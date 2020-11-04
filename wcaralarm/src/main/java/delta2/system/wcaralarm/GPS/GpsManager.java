package delta2.system.wcaralarm.GPS;

import android.content.Context;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageLocation;
import delta2.system.wcaralarm.Module;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;

public class GpsManager {

    Context mContext;
    static IRequestSendMessage requestSendMessage;

    static private Timer mTimer;
    static private MyTimerTask mMyTimerTask;

    public GpsManager(Context c, IRequestSendMessage msg){
        mContext = c;
        requestSendMessage = msg;
    }

    public void onDestroy(){
        stop();
    }


    public void start(){
        Helper.Log("gps", "start begin", true);
        stop();


        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask(mContext);

        if(mTimer != null)
            mTimer.schedule(mMyTimerTask, 100, 300);

        Helper.Log("gps", "start end", true);
    }

    public void  stop(){
        Helper.Log("gps", "stop begin", true);

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if(mMyTimerTask != null){
            mMyTimerTask.stopTmt();
            mMyTimerTask = null;
        }
        Helper.Log("gps", "stop end", true);
    }
    static double prev_latitude;
    static double prev_longitude;

    public static void getLoc(String msgId){
        if (requestSendMessage != null)
            requestSendMessage.RequestSendMessage(new MessageLocation(Module._MODULE_CODE, msgId, String.valueOf(prev_latitude), String.valueOf(prev_longitude)));
    }

    long _prevSend = 0;
    public class MyTimerTask extends TimerTask {

        GPS g;


        public MyTimerTask(Context c) {

            g = new GPS(c);
        }

        @Override
        public void run() {

            GPS_Result gps = g.getResult();

            long currentTime = Calendar.getInstance().getTimeInMillis();

            if(PreferencesHelper.getIsStarted() &&
               PreferencesHelper.getIsGpsActive() &&
               (currentTime - _prevSend > 10000)  && // 10sec
                     (gps.speed > PreferencesHelper.getGpsSpeed())
            ) {

                _prevSend = currentTime;

                requestSendMessage.RequestSendMessage(new MessageLocation(Module._MODULE_CODE, "", String.valueOf(gps.latitude), String.valueOf(gps.longitude)));
            }

            prev_latitude = gps.latitude;
            prev_longitude = gps.longitude;
        }

        public void stopTmt() {
            if (g != null) {
                g.stop();
                g = null;
            }
        }
    }

}

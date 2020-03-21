package delta2.system.wcaralarm.GPS;

import android.content.Context;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageLocation;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;

public class GpsManager {

    static GpsManager mGpsManager;

    Context mContext;
    IRequestSendMessage requestSendMessage;

    static private Timer mTimer;
    static private MyTimerTask mMyTimerTask;

    public enum en_mode{
        none,
        one,
        all,
        allone

    }

    en_mode mMode = en_mode.none;

    public GpsManager(Context c, IRequestSendMessage msg){
        mContext = c;
        requestSendMessage = msg;
    }

    public void onDestroy(){
        stop();
    }


    public void start() {
        start(en_mode.all);
    }


    public void start(en_mode m){
        Helper.Log("gps", "start begin", true);
        stop();

        if (mMode == en_mode.all && m == en_mode.one)
            m = en_mode.allone;

        mMode = m;

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask(mContext);

        if(mTimer != null)
            mTimer.schedule(mMyTimerTask, 100, 300);

        Helper.Log("gps", "start end", true);
    }

    public void  stop(){
        Helper.Log("gps", "stop begin", true);
        mMode = en_mode.none;

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

    public void getLoc(){
        start(en_mode.one);
    }

    long _prevSend = 0;
    public class MyTimerTask extends TimerTask {

        GPS g;
        int cntUp = 0;

        //  double prev_latitude;
        //  double prev_longitude;

        public MyTimerTask(Context c) {

            g = new GPS(c);
        }

        @Override
        public void run() {

            GPS_Result gps = g.getResult();

            long currentTime = Calendar.getInstance().getTimeInMillis();

            if(PreferencesHelper.getIsStarted() &&
               PreferencesHelper.getIsGpsActive() &&
               (currentTime - _prevSend > 30000) //&&
               //      (gps.speed > PreferencesHelper.getGpsSpeed() || mMode == en_mode.one ||mMode == en_mode.allone)
            ) {

                _prevSend = currentTime;

                requestSendMessage.RequestSendMessage(new MessageLocation("", String.valueOf(gps.latitude), String.valueOf(gps.longitude)));


                //  prev_latitude = gps.latitude;
                //  prev_longitude = gps.longitude;
            }

            if(mMode== en_mode.one)
                stop();
            if(mMode == en_mode.allone)
                mMode = en_mode.all;
        }

        public void stopTmt() {
            if (g != null) {
                g.stop();
                g = null;
            }
        }
    }

}

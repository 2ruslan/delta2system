package delta2.system.wcaralarm.Accelerometer;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageText;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;


public class AccelerationManager {

    static private Context mContext;
    static private Timer mTimer;
    static private MyTimerTask mMyTimerTask;

    IRequestSendMessage requestSendMessage;


    public static AccelerationManager mAccelerationManager;

    public AccelerationManager(Context c, IRequestSendMessage msg){
        mContext = c;
        requestSendMessage = msg;
    }



    boolean _isStarted;
    public void start(){

        Helper.Log("acc", "start begin", true);
        stop();

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask(mContext);

        if(mTimer != null)
            mTimer.schedule(mMyTimerTask, 100, 300);
        Helper.Log("acc", "start end", true);
        _isStarted = true;
    }

    public void  stop(){
        Helper.Log("acc", "stop begin", true);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if(mMyTimerTask != null){
            mMyTimerTask.stopTmt();
            mMyTimerTask = null;
        }
        Helper.Log("acc", "stop end", true);
        _isStarted = false;
    }


    public void onDestroy(){
        stop();
        mAccelerationManager = null;
    }


    public class MyTimerTask extends TimerTask {

        Accelerometer g;

        double prev_acc =0;

        public MyTimerTask(Context c) {

            g = new Accelerometer(c);
        }

        @Override
        public void run() {

            try {
                Accelerometer_Result res = g.getResult();

                prev_acc = res.acceleration;

                if (res.acceleration > PreferencesHelper.getAccLevel()) {
                    if (PreferencesHelper.getIsStarted() &&
                        PreferencesHelper.getIsAccActive()) {
                        requestSendMessage.RequestSendMessage( new MessageText(String.format("acceleration=%s", res.acceleration)));
                    }
                }


            }catch (Exception e){
                Helper.Ex2Log(e);
            }
        }

        public void stopTmt() {
            if (g != null) {
                g.stop();
                g = null;
            }
        }
    }

}


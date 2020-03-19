package delta2.system.wcaralarm.Accelerometer;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

import rock.delta2.carwatchdog.Helper;
import rock.delta2.carwatchdog.Mediator.MediatorMD;
import rock.delta2.carwatchdog.Preferences.PreferencesHelper;

public class AccelerationManager {

    static private Context mContext;
    static private Timer mTimer;
    static private MyTimerTask mMyTimerTask;


    public static AccelerationManager mAccelerationManager;

    public static AccelerationManager getInstance(Context c){
        if (mAccelerationManager == null)
            mAccelerationManager = new AccelerationManager(c);


        return mAccelerationManager;
    }

    protected AccelerationManager(Context c){
        mContext = c;
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


    public boolean GetIsActive(){
        return _isStarted;
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

                if (res.acceleration > PreferencesHelper.getAccLevel()) {
                    prev_acc = res.acceleration;
                    MediatorMD.setAcceleration(res.acceleration);

                    if (MediatorMD.isSendAcc() ) {
                        String msg = String.format("acceleration=%s", res.acceleration);
                        MediatorMD.sendText("0", msg);
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


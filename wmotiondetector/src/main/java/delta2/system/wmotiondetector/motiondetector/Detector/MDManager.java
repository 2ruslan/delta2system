package delta2.system.wmotiondetector.motiondetector.Detector;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;

import delta2.system.wmotiondetector.motiondetector.Common.RawPicture;
import delta2.system.wmotiondetector.motiondetector.Mediator.ICommandExcecuted;
import delta2.system.wmotiondetector.motiondetector.Mediator.IGetRawPictureCallback;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;
import delta2.system.wmotiondetector.motiondetector.commands.CmdCameraSet;
import delta2.system.wmotiondetector.motiondetector.commands.CmdCameraSizeSet;

public class MDManager implements IGetRawPictureCallback, ICommandExcecuted {
    private Context mContext;
    private SurfaceViewExt mMDCamera;
    private MD mMD;
    RawPicture _current;

    private Timer mTimer;
    private MDManagerTimerTask  mTimerTask;

    private int moveCnt = 0;
    private long lastSend = 0;

    @Override
    public void OnGetRawPicture() {
        try {

            if (mMD.chkImg(_current.data, _current.w, _current.h) >= PreferencesHelper.getDelta()) {
                if (mDetectNum == en_detect_mun.none)
                    mDetectNum = en_detect_mun.first;
                else if(mDetectNum == en_detect_mun.first) {
                    mDetectNum = en_detect_mun.detect;
                }

                if (mDetectNum == en_detect_mun.detect) {

                    long interval = 0;//PreferencesHelper.GetNotifyMinInterval();

                    if (interval== 0 ||
                          (Calendar.getInstance().getTimeInMillis() - lastSend) / 1000 > interval){

                        lastSend = Calendar.getInstance().getTimeInMillis();

                        String addInfo = String.format("∆ = %s", mMD.GetDelta());
                        Helper.Log("delta", addInfo);

                        //if (PreferencesHelper.GetInTwo())
                         //   MediatorMD.TexSendText(mContext.getString(R.string.move_description) + " " + addInfo, null);

                        SendPhoto.Send(_current.msgId, mContext, _current.getJpg(), addInfo, _current.h, _current.w);

                    }

                    if(++moveCnt > 2)
                        MediatorMD.callVoice();
                }
            }
            else if (mDetectNum != en_detect_mun.none)
            {
                mDetectNum = en_detect_mun.none;
                moveCnt = 0;
            }
        } catch (Exception e) {
            Helper.Ex2Log(e);
        }

    }



    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            String command = String.valueOf(message.obj);
            if( command.equals(CmdCameraSet._COMMAND)
                    || command.equals(CmdCameraSizeSet._COMMAND)
            ){
                timerStop();
                stopCamera();
                openCamera();
                timerStart();
            }
        }
    };

    Message message;

    @Override
    public void OnCommandExcecuted(String comand) {

        message = mHandler.obtainMessage(0, comand);
        message.sendToTarget();

    }

    enum en_detect_mun
    {
        none,
        first,
        detect
    }

    private en_detect_mun mDetectNum = en_detect_mun.none;

    public MDManager(Context context){
        mContext = context;
        _current = new RawPicture(this);

        mMD = new MD();

        MediatorMD.registerCommandExcecuted(this);

      //  if (PreferencesHelper.GetIsActive()) {
            openCamera();
            timerStart();
      //  }


    }

    public void onDestroy(){
        timerStop();

        MediatorMD.unregisterCommandExcecuted(this);

        stopCamera();

        if (mMD != null)
            mMD.onDestroy();
        mMD = null;



        mContext = null;
    }

    private void stopCamera(){
        if (mMDCamera != null)
            mMDCamera.onDestroy();
        mMDCamera = null;
    }

    private void openCamera(){

        CameraParameters p = new CameraParameters();
        p.camIdx = PreferencesHelper.getCameraIdx();
        p.sizeIdx = PreferencesHelper.getCameraSizeIdx();

        mMDCamera = new SurfaceViewExt(mContext, p);
    }

    private void timerStart(){
        mTimer = new Timer();
        mTimerTask = new MDManagerTimerTask(this);
        mTimer.schedule(mTimerTask, 1000, 400);
    }

    private void timerStop(){
        if (mTimer != null)
            mTimer.cancel();

        mTimer = null;
        mTimerTask = null;
    }

    class MDManagerTimerTask extends TimerTask {

        MDManager mMDManager;

        public MDManagerTimerTask(MDManager m){
            mMDManager = m;
        }

        @Override
        public void run() {
            if(PreferencesHelper.GetIsActive()) {
                MediatorMD.GetRawPciture(mMDManager._current,  true);
            }
        }
    }
}

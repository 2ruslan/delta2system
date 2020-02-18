package delta2.system.wmotiondetector.motiondetector;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;
import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraAngleSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSizeSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdDeltaSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdStart;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdStop;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdTurn;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdVoiceCallSet;
import delta2.system.wmotiondetector.motiondetector.Common.RawPicture;
import delta2.system.wmotiondetector.motiondetector.Detector.CamearaProps;
import delta2.system.wmotiondetector.motiondetector.Mediator.ICameraStarted;
import delta2.system.wmotiondetector.motiondetector.Mediator.ICommandExcecuted;
import delta2.system.wmotiondetector.motiondetector.Mediator.IGetRawPictureCallback;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class SettingsActivity extends Activity
        implements IGetRawPictureCallback, ICommandExcecuted, ICameraStarted, AdapterView.OnItemSelectedListener {

    private Timer mTimer;
    private TimerTaskPict mTimerTask;
    RawPicture _current;


    private SurfaceView sfPreviw;

    CheckBox cbVoiceCall;
    CheckBox cbShowPreview;
    Spinner spCamera;
    Spinner spCameraSize;
    Spinner spCameraAngle;
    Button btStartStop;

    EditText edDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motiondetector_settings);

        PreferencesHelper.init(this);

        MediatorMD.registerCommandExcecuted(this);
        MediatorMD.setOnCameraStarted(this);

        sfPreviw = findViewById(R.id.sfPrev);
        cbVoiceCall = findViewById(R.id.cbVoiceCall);


        spCamera = findViewById(R.id.spCamera);
        spCamera.setOnItemSelectedListener(this);

        spCameraSize = findViewById(R.id.spCameraSize);
        spCameraSize.setOnItemSelectedListener(this);

        spCameraAngle = findViewById(R.id.spCameraAngle);
        spCameraAngle.setOnItemSelectedListener(this);

        btStartStop = findViewById(R.id.btStartStop);

        edDelta = findViewById(R.id.edDelta);

        cbShowPreview = findViewById(R.id.cbShowPreview);

        refresh("");
    }


    @Override
    public void onStart(){
        super.onStart();

        _current = new RawPicture(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        MediatorMD.unregisterCommandExcecuted(this);

        if (mTimer != null)
            mTimer.cancel();

        mTimerTask = null;
        mTimer = null;

        _current.OnDestroy();
        _current = null;

    }

    @Override
    public void OnGetRawPicture() {
        runOnUiThread( new DrawPicture(this));

        if(sfPreviw != null && _current != null){
            Canvas canvas = null;
            SurfaceHolder surfaceHolder =  sfPreviw.getHolder();
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas!= null) {
                    synchronized (surfaceHolder) {
                        canvas.drawColor(Color.BLACK);
                        canvas.drawBitmap(
                                _current.getBitmapPreview(sfPreviw.getWidth()
                                        , sfPreviw.getHeight()), new Matrix(), null);
                    }
                }
            }
            catch (Exception ex){
                Helper.Ex2Log(ex);
            }
            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    @Override
    public void OnCommandExcecuted(String comand) {
        refresh(comand);
    }

    private void refresh(String prop){
        boolean isAll = prop.equals("");


        if (isAll || prop.equals(CmdStart._COMMAND) || prop.equals(CmdStop._COMMAND) || prop.equals(CmdTurn._COMMAND) )
            setStartStop();

        if (isAll || prop.equals(CmdVoiceCallSet._COMMAND))
            cbVoiceCall.setChecked(PreferencesHelper.getIsVoiceCall());

        if (isAll || prop.equals(CmdCameraSet._COMMAND))
            refreshSpinner(spCamera, CmdCameraGet._COMMAND);

        if (isAll || prop.equals(CmdCameraSizeSet._COMMAND))
            refreshSpinner(spCameraSize, CmdCameraSizeSet._COMMAND);

        if (isAll || prop.equals(CmdCameraSizeSet._COMMAND))
            refreshSpinner(spCameraAngle, CmdCameraAngleSet._COMMAND);

        if (isAll || prop.equals(CmdDeltaSet._COMMAND))
            edDelta.setText( String.valueOf(PreferencesHelper.getDelta()));


    }

    private void setStartStop(){
        if(PreferencesHelper.GetIsActive())
            btStartStop.setText(R.string.stop);
        else
            btStartStop.setText(R.string.start);
    }

    private void refreshSpinner(Spinner s, String p){

        try {
            CamearaProps prop = MediatorMD.GetCameraProps(p);
            if (prop == null)
                return;

            ArrayAdapter<String> a = new ArrayAdapter(this, android.R.layout.simple_spinner_item, prop.values);

            s.setAdapter(a);

            s.setSelection(prop.current);
        }catch (Exception e){
            Helper.Ex2Log(e);
        }

    }

    public void onClick(View view) {
       if (view.equals(cbShowPreview))
            start_stop_preview(cbShowPreview.isChecked());

    }

    public void onMinimizeClick(View view) {
        finish();
    }


    public void onStartStopClick(View view) {
        MediatorMD.CheckMessage("",PreferencesHelper.GetIsActive()? CmdStop._COMMAND : CmdStart._COMMAND);

    }

    public void onClickOkDelta(View view) {
        MediatorMD.CheckMessage("",String.format("set %s %s", CmdDeltaSet._COMMAND, edDelta.getText()));
    }



    @Override
    public void OnCameraStartted(boolean isStarted) {
        if(isStarted)
            refresh("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.equals(spCamera) && PreferencesHelper.getCameraIdx() != position)
            MediatorMD.CheckMessage("",String.format("set %s %s", CmdCameraSet._COMMAND, position));
        else if(parent.equals(spCameraSize) && PreferencesHelper.getCameraSizeIdx() != position)
            MediatorMD.CheckMessage("",String.format("set %s %s", CmdCameraSizeSet._COMMAND, position));
        else if(parent.equals(spCameraAngle) && PreferencesHelper.getCameraAngleIdx() != position)
            MediatorMD.CheckMessage("",String.format("set %s %s", CmdCameraAngleSet._COMMAND, position));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void start_stop_preview(boolean isShow){

        if(isShow){
            mTimer = new Timer();
            mTimerTask = new TimerTaskPict(_current);
            mTimer.schedule(mTimerTask, 0, 400);
        }
        else {
            mTimer.cancel();
            mTimer = null;
            mTimerTask = null;
        }


    }

    //region camera preview
    static class DrawPicture implements Runnable {

        SettingsActivity activity;

        public DrawPicture(SettingsActivity a){
            activity = a;
        }

        @Override
        public void run() {
            if(activity.sfPreviw != null && activity._current != null){
                Canvas canvas = null;
                SurfaceHolder surfaceHolder =  activity.sfPreviw.getHolder();
                try {
                    // получаем объект Canvas и выполняем отрисовку
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas!= null) {
                        synchronized (surfaceHolder) {
                            canvas.drawColor(Color.BLACK);
                            canvas.drawBitmap(
                                    activity._current.getBitmapPreview(activity.sfPreviw.getWidth()
                                            , activity.sfPreviw.getHeight()), new Matrix(), null);
                        }
                    }
                }
                catch (Exception ex){
                    Helper.Ex2Log(ex);
                }
                finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    class TimerTaskPict extends TimerTask {

        RawPicture _pic;

        public TimerTaskPict(RawPicture pic){
            _pic = pic;
        }

        @Override
        public void run() {
            MediatorMD.GetRawPciture(_pic, true);
        }
    }
//endregion camera preview

}

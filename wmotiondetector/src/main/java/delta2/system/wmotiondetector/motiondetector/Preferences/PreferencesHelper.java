package delta2.system.wmotiondetector.motiondetector.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "wmotiondetector.preference";

    public static final String DELTA = "DELTA";
    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String IS_ACTIVE = "IS_ACTIVE";
    public static final String VOICE_CALL = "VOICE_CALL";

    public static final String CAMERA_IDX = "CAMERA_IDX";
    public static final String CAMERA_SIZE_IDX = "CAMERA_SIZE_IDX";
    public static final String CAMERA_ANGLE_IDX = "CAMERA_ANGLE_IDX";

    public static final String FRAME_INTERVAL = "FRAME_INTERVAL";


    private static SharedPreferences mSettings;

    private static PreferenceValue _isActive;
    private static PreferenceValue _isVoiceCall;
    private static PreferenceValue _delta;
    private static PreferenceValue _deviceName;

    private static PreferenceValue _cameraIdx;
    private static PreferenceValue _cameraSizeIdx;
    private static PreferenceValue _cameraAngleIdx;

    private static PreferenceValue _frameInterval;


    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _isActive  = new PreferenceValue(mSettings, IS_ACTIVE, true);

        _isVoiceCall = new PreferenceValue(mSettings, VOICE_CALL, true);
        _delta = new PreferenceValue(mSettings, DELTA, 8);
        _deviceName = new PreferenceValue(mSettings, DEVICE_NAME, "");

        _cameraIdx = new PreferenceValue(mSettings, CAMERA_IDX, 0);
        _cameraSizeIdx = new PreferenceValue(mSettings, CAMERA_SIZE_IDX, 0);
        _cameraAngleIdx =  new PreferenceValue(mSettings, CAMERA_ANGLE_IDX, 0);
    }

    //region IS_ACTIVE
    public static void SetIsActive(boolean isActive) {
        _isActive.setBool(isActive);
    }

    public static boolean GetIsActive(){
        return _isActive.getBool();
    }
    // endregion IS_ACTIVE

    //region VOICE_CALL
    public static void setIsVoiceCall(boolean isActive) {
        _isVoiceCall.setBool(isActive);
    }

    public static boolean getIsVoiceCall(){
        return _isVoiceCall.getBool();
    }
    // endregion VOICE_CALL

    //region DELTA
    public static void setDelta(int val) {
        _delta.setInt(val);
    }

    public static int getDelta() {
        return _delta.getInt();
    }
    //endregion DELTA

    //region DEVICE_NAME
    public static void setDeviceName(String val) {
        _deviceName.setStr(val);
    }

    public static String getDeviceName() {
        return _deviceName.getStr();
    }
    //endregion DELTA

    //region CAMERA_IDX
    public static void setCameraIdx(int val) {
        _cameraIdx.setInt(val);
    }

    public static int getCameraIdx() {
        return _cameraIdx.getInt();
    }
    //endregion CAMERA_IDX

    //region CAMERA_SIZE_IDX
    public static void setCameraSizeIdx(int val) {
        _cameraSizeIdx.setInt(val);
    }

    public static int getCameraSizeIdx() {
        return _cameraSizeIdx.getInt();
    }
    //endregion CAMERA_SIZE_IDX

    //region CAMERA_ANGLE_IDX
    public static void setCameraAngleIdx(int val) {
        _cameraAngleIdx.setInt(val);
    }

    public static int getCameraAngleIdx() {
        return _cameraAngleIdx.getInt();
    }
    //endregion CAMERA_ANGLE_IDX



}

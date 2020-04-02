package delta2.system.wcaralarm.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "preference";

    public static final String IS_STARTED = "IS_STARTED";

    public static final String ACC_LEVEL = "ACC_LEVEL";
    public static final String GPS_SPEED = "GPS_SPEED";

    public static final String GPS_ACTIVE = "GPS_ACTIVE";
    public static final String ACC_ACTIVE = "ACC_ACTIVE";


    private static SharedPreferences mSettings;

    private static PreferenceValue _isStarted;

    private static PreferenceValue _isGpsActive;
    private static PreferenceValue _isAccActive;

    private static PreferenceValue _accLevel;
    private static PreferenceValue _gpsSpeed;

    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _isStarted = new PreferenceValue(mSettings, IS_STARTED, false);

        _isGpsActive = new PreferenceValue(mSettings, GPS_ACTIVE, true);
        _isAccActive = new PreferenceValue(mSettings, ACC_ACTIVE, true);

        _accLevel = new PreferenceValue(mSettings, ACC_LEVEL, 1.0f);
        _gpsSpeed = new PreferenceValue(mSettings, GPS_SPEED, 10);

    }

    //region IS_STARTED
    public static void setIsStarted(boolean val) {
        _isStarted.setBool(val);
    }

    public static boolean getIsStarted() {
        return _isStarted.getBool();
    }
    //endregion IS_STARTED

    //region GPS_ACTIVE
    public static void setIsGpsActive(boolean val) {
        _isGpsActive.setBool(val);
    }

    public static boolean getIsGpsActive() {
        return _isGpsActive.getBool();
    }
    //endregion GPS_ACTIVE

    //region ACC_ACTIVE
    public static void setIsAccActive(boolean isActive) {
        _isAccActive.setBool(isActive);
    }

    public static boolean getIsAccActive(){
        return _isAccActive.getBool();
    }
    // endregion ACC_ACTIVE


    //region ACC_LEVEL
    public static void setAccLevel(float val) {
        _accLevel.setFloat(val);
    }

    public static float getAccLevel() {
        return _accLevel.getFloat();
    }
    //endregion ACC_LEVEL


    //region GPS_SPEED
    public static void setGpsSpeed(int val) {
        _gpsSpeed.setInt(val);
    }

    public static int getGpsSpeed() {
        return _gpsSpeed.getInt();
    }
    //endregion GPS_SPEED



}

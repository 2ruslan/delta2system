package delta2.system.warduinobridge.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;


public class PreferencesHelper {

    public static final String APP_PREFERENCES = "preference";

    public static final String AUTO_START = "AUTO_START";
    public static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String DEVICE_NAME = "DEVICE_NAME";



    private static SharedPreferences mSettings;
    private static PreferenceValue _autoStart;

    private static PreferenceValue _deviceAddress;
    private static PreferenceValue _deviceName;

    public static void init(Context context) {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _autoStart  = new PreferenceValue(mSettings, AUTO_START, true);

        _deviceAddress = new PreferenceValue(mSettings, DEVICE_ADDRESS, "");
        _deviceName= new PreferenceValue(mSettings, DEVICE_NAME, "");
    }

    //region DEVICE_NAME
    public static void setDeviceName(String val) {
        _deviceName.setStr(val);
    }

    public static String getDeviceName() {
        return _deviceName.getStr();
    }
    //endregion DEVICE_NAME

    //region DEVICE_ADDRESS
    public static void setDeviceAddress(String val) {
        _deviceAddress.setStr(val);
    }

    public static String getDeviceAddress() {
        return _deviceAddress.getStr();
    }
    //endregion DEVICE_ADDRESS

    //region AUTO_START
    public static void setAutoStart(boolean val) {
        _autoStart.setBool(val);
    }

    public static boolean getAutoStart() {
        return _autoStart.getBool();
    }
    //endregion AUTO_START

}

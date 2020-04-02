package delta2.system.whardwareinfo.hardwareinfo.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "whardwareinfo.preference";

    public static final String NOTIFY_POWER = "NOTIFY_POWER";
    public static final String NOTIFY_CONNECTION = "NOTIFY_CONNECTION";


    private static SharedPreferences mSettings;
    private static PreferenceValue _notifyPower;
    private static PreferenceValue _notifyConnection;

    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _notifyPower = new PreferenceValue(mSettings, NOTIFY_POWER, true);
        _notifyConnection= new PreferenceValue(mSettings, NOTIFY_CONNECTION, true);

    }


    //region NOTIFY_POWER
    public static void setNotifyPower(boolean val) {
        _notifyPower.setBool(val);
    }

    public static boolean getNotifyPower() {
        return _notifyPower.getBool();
    }
    //endregion NOTIFY_POWER


    //region NOTIFY_CONNECTION
    public static void setNotifyConnection(boolean val) {
        _notifyConnection.setBool(val);
    }

    public static boolean getNotifyConnection() {
        return _notifyConnection.getBool();
    }
    //endregion NOTIFY_CONNECTION
}

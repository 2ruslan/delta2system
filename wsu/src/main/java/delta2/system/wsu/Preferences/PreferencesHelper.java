package delta2.system.wsu.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "wsu.preference";

    public static final String LAST_REBOOT_TIME = "LAST_REBOOT_TIME";


    private static SharedPreferences mSettings;
    private static PreferenceValue _lastRebootTime;


    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _lastRebootTime = new PreferenceValue(mSettings, LAST_REBOOT_TIME, 0L);

    }


    //region LAST_REBOOT_TIME
    public static void setLastRebootTime(long val) {
        _lastRebootTime.setLong(val);
    }

    public static long getLastRebootTime() {
        return _lastRebootTime.getLong();
    }
    //endregion LAST_REBOOT_TIME

}

package delta2.system.delta2system;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;


public class PreferencesHelper {

    public static final String APP_PREFERENCES = "preference";

    public static final String AUTO_START = "AUTO_START";


    private static SharedPreferences mSettings;
    private static PreferenceValue _autoStart;


    public static void init(Context context) {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _autoStart  = new PreferenceValue(mSettings, AUTO_START, true);

    }


    //region AUTO_START
    public static void setAutoStart(boolean val) {
        _autoStart.setBool(val);
    }

    public static boolean getAutoStart() {
        return _autoStart.getBool();
    }
    //endregion AUTO_START

    public static boolean getIsActiveModule(String moduleId){
        PreferenceValue p = new PreferenceValue(mSettings, moduleId, false);
        return p.getBool();
    }

    public static void setIsActiveModule(String moduleId, boolean isActive){
        PreferenceValue p = new PreferenceValue(mSettings, moduleId, false);
        p.setBool(isActive);
    }

}

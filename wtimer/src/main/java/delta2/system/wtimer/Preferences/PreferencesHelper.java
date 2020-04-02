package delta2.system.wtimer.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "wtimer.preference";

    public static final String COMMANDS = "COMMANDS";

    private static SharedPreferences mSettings;

    private static PreferenceValue _commands;

    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _commands = new PreferenceValue(mSettings, COMMANDS, "");

    }

    //region COMMANDS
    public static void setCommands(String val) {
        _commands.setStr(val);
    }

    public static String getCommands() {
        return _commands.getStr();
    }
    //endregion COMMANDS

}

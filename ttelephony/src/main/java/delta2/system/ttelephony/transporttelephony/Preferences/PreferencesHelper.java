package delta2.system.ttelephony.transporttelephony.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "preference";

    public static final String SEND_TEXT = "SEND_TEXT";
    public static final String VOICE_CALL = "VOICE_CALL";
    public static final String PHONE_NUM = "PHONE_NUM";


    private static SharedPreferences mSettings;

    private static PreferenceValue _sendText;
    private static PreferenceValue _voiceCall;
    private static PreferenceValue _phoneNum;

    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _sendText = new PreferenceValue(mSettings, SEND_TEXT, true);

        _voiceCall = new PreferenceValue(mSettings, VOICE_CALL, true);
        _phoneNum = new PreferenceValue(mSettings, PHONE_NUM, "");

    }

    //region SEND_TEXT
    public static void setSendText(boolean val) {
        _sendText.setBool(val);
    }

    public static boolean getSendText() {
        return _sendText.getBool();
    }
    //endregion SEND_TEXT

    //region SEND_FILE
    public static void setVoiceCall(boolean val) {
        _voiceCall.setBool(val);
    }

    public static boolean getVoiceCall() {
        return _voiceCall.getBool();
    }
    //endregion SEND_FILE

    //region PHONE_NUM
    public static void setPhoneNum(String val) {
        _phoneNum.setStr(val);
    }

    public static String getPhoneNum() {
        return _phoneNum.getStr();
    }
    //endregion PHONE_NUM
}

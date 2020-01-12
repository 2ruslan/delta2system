package delta2.system.ttelegram.transporttelegram.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;

public class PreferencesHelper {

    public static final String APP_PREFERENCES = "ttelegram.preference";


    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String AUTO_START = "AUTO_START";
    public static final String PHONE_NUM = "PHONE_NUM";
    public static final String CHAT_ID = "CHAT_ID";
    public static final String MY_ID = "MY_ID";
    public static final String DEL_HIST = "DEL_HIST";
    public static final String NOTIFY_ONLINE = "NOTIFY_ONLINE";

    public static final String SEND_TEXT = "SEND_TEXT";
    public static final String SEND_FILE = "SEND_FILE";
    public static final String SEND_PHOTO = "SEND_PHOTO";
    public static final String SEND_LOCATION = "SEND_LOCATION";

    private static SharedPreferences mSettings;

    private static PreferenceValue _deviceName;
    private static PreferenceValue _phoneNum;
    private static PreferenceValue _chatId;
    private static PreferenceValue _myId;
    private static PreferenceValue _notifyOnline;

    private static PreferenceValue _sendText;
    private static PreferenceValue _sendFile;
    private static PreferenceValue _sendPhoto;
    private static PreferenceValue _sendLocation;


    public static void init(Context context) {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _deviceName = new PreferenceValue(mSettings, DEVICE_NAME, "");

        _phoneNum = new PreferenceValue(mSettings, PHONE_NUM, "");
        _chatId = new PreferenceValue(mSettings, CHAT_ID, -1);
        _myId = new PreferenceValue(mSettings, MY_ID, -1);
        _notifyOnline = new PreferenceValue(mSettings, NOTIFY_ONLINE, true);

        _sendText = new PreferenceValue(mSettings, SEND_TEXT, true);
        _sendFile = new PreferenceValue(mSettings, SEND_FILE, true);
        _sendPhoto = new PreferenceValue(mSettings, SEND_PHOTO, true);
        _sendLocation = new PreferenceValue(mSettings, SEND_LOCATION, true);

    }


    //region DEVICE_NAME
    public static void setDeviceName(String val) {
        _deviceName.setStr(val);
    }

    public static String getDeviceName() {
        return _deviceName.getStr();
    }
    //endregion DEVICE_NAME

    //region NOTIFY_ONLINE
    public static void SetNotifyOnline(boolean val) {
        _notifyOnline.setBool(val);
    }

    public static boolean GetNotifyOnline() {
        return _notifyOnline.getBool();
    }
    //endregion NOTIFY_ONLINE

    //region PHONE_NUM
    public static void SetPhoneNum(String val) {
        _phoneNum.setStr(val);
    }

    public static String getPhoneNum() {
        return _phoneNum.getStr();
    }
    //endregion PHONE_NUM

    //region CODE
    public static String code = "";
    //endregion CODE

    //region PASS
    public static String pass = "";
    //endregion PASS

    //region CHAT_ID
    public static void SetChatId(long val) {
        _chatId.setLong(val);
    }

    public static long getChatId() {
        return _chatId.getLong();
    }

    public static boolean existsChatId(){
        return getChatId() != 0;
    }
    //endregion CHAT_ID

    //region MY_ID
    public static void SetMyId(int val) {
        _myId.setInt(val);
    }

    public static int getMyId() {
        return _myId.getInt();
    }
    //endregion MY_ID

    //region SEND_TEXT
    public static void setSendText(boolean val) {
        _sendText.setBool(val);
    }

    public static boolean getSendText() {
        return _sendText.getBool();
    }
    //endregion SEND_TEXT

    //region SEND_FILE
    public static void setSendFile(boolean val) {
        _sendFile.setBool(val);
    }

    public static boolean getSendFile() {
        return _sendFile.getBool();
    }
    //endregion SEND_FILE

    //region SEND_PHOTO
    public static void setSendPhoto(boolean val) {
        _sendPhoto.setBool(val);
    }

    public static boolean getSendPhoto() {
        return _sendPhoto.getBool();
    }
    //endregion SEND_PHOTO

    //region SEND_LOCATION
    public static void setSendLocation(boolean val) {
        _sendLocation.setBool(val);
    }

    public static boolean getSendLocation() {
        return _sendLocation.getBool();
    }
    //endregion SEND_LOCATION

}

package delta2.system.tmail.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "preference";

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String DEVICE_NAME = "DEVICE_NAME";

    public static final String SEND_TEXT = "SEND_TEXT";
    public static final String SEND_FILE = "SEND_FILE";
    public static final String SEND_PHOTO = "SEND_PHOTO";

    public static final String FTP_IP = "FTP_IP";
    public static final String FTP_PORT = "FTP_PORT";
    public static final String FTP_USR = "FTP_USR";
    public static final String FTP_PASS = "FTP_PASS";
    public static final String FTP_PATH = "FTP_PATH";


    private static SharedPreferences mSettings;
    private static PreferenceValue _deviceName;
    private static PreferenceValue _sendText;
    private static PreferenceValue _sendFile;
    private static PreferenceValue _sendPhoto;

    private static PreferenceValue _ftpIp;
    private static PreferenceValue _ftpPort;
    private static PreferenceValue _ftpUsr;
    private static PreferenceValue _ftpPass;
    private static PreferenceValue _ftpPath;


    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _deviceName = new PreferenceValue(mSettings, DEVICE_NAME, "");

        _sendText = new PreferenceValue(mSettings, SEND_TEXT, true);
        _sendFile = new PreferenceValue(mSettings, SEND_FILE, true);
        _sendPhoto = new PreferenceValue(mSettings, SEND_PHOTO, true);

        _ftpIp  = new PreferenceValue(mSettings, FTP_IP, "");
        _ftpUsr = new PreferenceValue(mSettings, FTP_USR, "");
        _ftpPass = new PreferenceValue(mSettings, FTP_PASS, "");
        _ftpPort = new PreferenceValue(mSettings, FTP_PORT, 21);
        _ftpPath = new PreferenceValue(mSettings, FTP_PATH, "");

    }

    //region FTP_IP
    public static void setFtpId(String val) {
        _ftpIp.setStr(val);
    }

    public static String getFtpId() {
        return _ftpIp.getStr();
    }
    //endregion FTP_IP

    //region FTP_PORT
    public static void setFtpPort(int val) {
        _ftpPort.setInt(val);
    }

    public static int getFtpPort() {
        return _ftpPort.getInt();
    }
    //endregion FTP_PORT

    //region FTP_PATH
    public static void setFtpPath(String val) {
        _ftpPath.setStr(val);
    }

    public static String getFtpPath() {
        return _ftpPath.getStr();
    }
    //endregion FTP_PATH

    //region FTP_USR
    public static void setFtpUsr(String val) {
        _ftpUsr.setStr(val);
    }

    public static String getFtpUsr() {
        return _ftpUsr.getStr();
    }
    //endregion FTP_USR

    //region FTP_PASS
    public static void setFtpPass(String val) {
        _ftpPass.setStr(val);
    }

    public static String getFtpPass() {
        return _ftpPass.getStr();
    }
    //endregion FTP_PASS


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
}

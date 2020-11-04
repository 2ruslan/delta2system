package delta2.system.warduinobridge.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import delta2.system.common.preferences.PreferenceValue;
import delta2.system.common.preferences.PreferencesHelperBase;


public class PreferencesHelper extends PreferencesHelperBase {

    public static final String APP_PREFERENCES = "preference";

    public static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String DEVICE_NAME = "DEVICE_NAME";

    public static final String MQTT_ADDR = "MQTT_ADDR";
    public static final String MQTT_USER = "MQTT_USER";
    public static final String MQTT_PASS = "MQTT_PASS";



    private static SharedPreferences mSettings;

    private static PreferenceValue _deviceAddress;
    private static PreferenceValue _deviceName;

    private static PreferenceValue _mqttAddr;
    private static PreferenceValue _mqttUser;
    private static PreferenceValue _mqttPass;

    static {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _deviceAddress = new PreferenceValue(mSettings, DEVICE_ADDRESS, "");
        _deviceName = new PreferenceValue(mSettings, DEVICE_NAME, "");

        _mqttAddr = new PreferenceValue(mSettings, MQTT_ADDR, "");
        _mqttUser = new PreferenceValue(mSettings, MQTT_USER, "");
        _mqttPass = new PreferenceValue(mSettings, MQTT_PASS, "");
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

    //region MQTT_ADDR
    public static void setMqttAdr(String val) {
        _mqttAddr.setStr(val);
    }

    public static String getMqttAdr() {
        return _mqttAddr.getStr();
    }
    //endregion MQTT_ADDR

    //region MQTT_USER
    public static void setMqttUser(String val) {
        _mqttUser.setStr(val);
    }

    public static String getMqttUser() {
        return _mqttUser.getStr();
    }
    //endregion MQTT_USER

    //region MQTT_PASS
    public static void setMqttPass(String val) {
        _mqttPass.setStr(val);
    }

    public static String getMqttPass() {
        return _mqttPass.getStr();
    }
    //endregion MQTT_PASS

}

package delta2.system.warduinobridge;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import delta2.system.common.Log.L;
import delta2.system.warduinobridge.Preferences.PreferencesHelper;

public class MQTT {

    public static void sendData(String topic, String value){
        /*
        try {
            MqttClient mqttClient = new MqttClient(PreferencesHelper.getMqttAdr(),
                    Module._ID,// MqttClient.generateClientId(),
                    new MemoryPersistence());

            MqttConnectOptions opt = new MqttConnectOptions();
            opt.setCleanSession(true);
            opt.setUserName(PreferencesHelper.getMqttUser());
            opt.setPassword(PreferencesHelper.getMqttPass().toCharArray());
            mqttClient.connect(opt);

            mqttClient.publish(topic, value.getBytes(), 1, true);

            mqttClient.close();
        } catch (Exception e)
        {
            L.log.error(e.getMessage());
        }
        */

    }

}

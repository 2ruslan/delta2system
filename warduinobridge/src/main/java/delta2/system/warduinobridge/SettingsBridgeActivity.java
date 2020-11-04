package delta2.system.warduinobridge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import delta2.system.warduinobridge.Preferences.PreferencesHelper;

public class SettingsBridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_bridge);
        ShowBTDevice();
    }

    public void onConnect(View v) {
        BluetoothAdapter bluetooth;
        final ArrayList<String> deviceStrs = new ArrayList<String>();
        final ArrayList<String> devices = new ArrayList<String>();

        bluetooth = BluetoothAdapter.getDefaultAdapter();


        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceStrs.add(device.getName() + "\n" + device.getAddress());
                devices.add(device.getAddress());
            }
        }

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,
                deviceStrs.toArray(new String[deviceStrs.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        String deviceAddress = devices.get(position);
                        String deviceName = deviceStrs.get(position);

                        PreferencesHelper.setDeviceAddress(deviceAddress);
                        PreferencesHelper.setDeviceName(deviceName);

                        ShowBTDevice();

                    }
                }
        );
        alertDialog.setTitle("Choose Bluetooth device");
        alertDialog.show();
    }


    private void ShowBTDevice(){
        TextView tv = findViewById(R.id.tvBTDevice);
        tv.setText(PreferencesHelper.getDeviceName());

        tv = findViewById(R.id.tvMqttPass);
        tv.setText(PreferencesHelper.getMqttPass());

        tv = findViewById(R.id.tvMqttUser);
        tv.setText(PreferencesHelper.getMqttUser());

        tv = findViewById(R.id.tvMqttAddr);
        tv.setText(PreferencesHelper.getMqttAdr());
    }

    public void onMinimizeClick(View view) {
        finish();
    }

    public void onMqttAddr(View v) {
        PreferencesHelper.setMqttAdr( ((EditText)findViewById(R.id.tvMqttAddr)).getText().toString());
    }

    public void onMqttUser(View v) {
        PreferencesHelper.setMqttUser( ((EditText)findViewById(R.id.tvMqttUser)).getText().toString());
    }

    public void onMqttPass(View v) {
        PreferencesHelper.setMqttPass( ((EditText)findViewById(R.id.tvMqttPass)).getText().toString());
    }

}

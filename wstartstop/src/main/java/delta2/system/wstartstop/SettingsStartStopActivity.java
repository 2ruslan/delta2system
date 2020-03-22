package delta2.system.wstartstop;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

import delta2.system.wstartstop.Preferences.PreferencesHelper;

public class SettingsStartStopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_startstop);
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
    }

    public void onMinimizeClick(View view) {
        finish();
    }
}

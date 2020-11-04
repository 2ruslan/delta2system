package delta2.system.whardwareinfo.hardwareinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.whardwareinfo.R;
import delta2.system.whardwareinfo.hardwareinfo.Preferences.PreferencesHelper;


public class SettingsActivity extends AppCompatActivity {

    CheckBox cbNotifyPower;
    CheckBox cbNotifyConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardawareinfo_settings);

        cbNotifyPower = findViewById(R.id.cbNotifyPower);
        cbNotifyConnection = findViewById(R.id.cbNotifyConnection);

        init();
    }

    private void init(){
        cbNotifyPower.setChecked(PreferencesHelper.getNotifyPower());
        cbNotifyConnection.setChecked(PreferencesHelper.getNotifyConnection());

    }


    public void onClick(View view) {
        if (view.equals(cbNotifyPower))
            PreferencesHelper.setNotifyPower(cbNotifyPower.isChecked());
        else if (view.equals(cbNotifyConnection))
            PreferencesHelper.setNotifyConnection(cbNotifyConnection.isChecked());

    }


    public void onMinimizeClick(View view) {
        finish();
    }
}

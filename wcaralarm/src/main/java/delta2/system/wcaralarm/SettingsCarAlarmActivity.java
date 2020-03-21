package delta2.system.wcaralarm;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.wcaralarm.Preferences.PreferencesHelper;


public class SettingsCarAlarmActivity extends AppCompatActivity {

    CheckBox cbUseGps;
    CheckBox cbUseAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caralarm_settings);

        cbUseGps = findViewById(R.id.cbUseGps);
        cbUseAcc = findViewById(R.id.cbUseAcc);

        init();
    }

    private void init(){
        cbUseGps.setChecked(PreferencesHelper.getIsGpsActive());
        cbUseAcc.setChecked(PreferencesHelper.getIsAccActive());

    }


    public void onClick(View view) {
        if (view.equals(cbUseAcc))
            PreferencesHelper.setIsAccActive(cbUseAcc.isChecked());
        else if (view.equals(cbUseGps))
            PreferencesHelper.setIsGpsActive(cbUseGps.isChecked());

    }


    public void onMinimizeClick(View view) {
        finish();
    }
}

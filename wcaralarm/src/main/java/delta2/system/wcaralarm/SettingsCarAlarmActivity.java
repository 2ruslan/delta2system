package delta2.system.wcaralarm;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.common.Log.L;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;


public class SettingsCarAlarmActivity extends AppCompatActivity {

    CheckBox cbUseGps;
    CheckBox cbUseAcc;

    EditText edSpeed;
    EditText edAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caralarm_settings);

        cbUseGps = findViewById(R.id.cbUseGps);
        cbUseAcc = findViewById(R.id.cbUseAcc);

        edSpeed = findViewById(R.id.etSpeed);
        edAcc = findViewById(R.id.etAcceleration);

        init();
    }

    private void init(){
        cbUseGps.setChecked(PreferencesHelper.getIsGpsActive());
        cbUseAcc.setChecked(PreferencesHelper.getIsAccActive());

        edSpeed.setText(String.valueOf(PreferencesHelper.getGpsSpeed()));
        edAcc.setText(String.valueOf(PreferencesHelper.getAccLevel()));

    }


    public void onClick(View view) {
        if (view.equals(cbUseAcc))
            PreferencesHelper.setIsAccActive(cbUseAcc.isChecked());
        else if (view.equals(cbUseGps))
            PreferencesHelper.setIsGpsActive(cbUseGps.isChecked());

    }

    public void  onSaveSpeedClick(View view){
        try {
            PreferencesHelper.setGpsSpeed(Integer.valueOf(edSpeed.getText().toString()));
        }catch (Exception ex)
        {
            L.log.error("", ex);
        }

    }

    public void  onSaveAccClick(View view){
        try {
            PreferencesHelper.setAccLevel(Float.valueOf(edAcc.getText().toString()));
        }catch (Exception ex)
        {
            L.log.error("", ex);
        }

    }

    public void onMinimizeClick(View view) {
        finish();
    }
}

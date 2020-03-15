package delta2.system.ttelephony.transporttelephony;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.ttelephony.R;
import delta2.system.ttelephony.transporttelephony.Preferences.PreferencesHelper;


public class SettingsActivity extends AppCompatActivity {

    CheckBox cbText;
    CheckBox cbVoiceCall;
    EditText etPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cbText  = findViewById(R.id.cbText);
        cbVoiceCall  = findViewById(R.id.cbVoiceCall);
        etPhone = findViewById(R.id.etPhone);

        init();
    }

    private void init(){
        cbText.setChecked(PreferencesHelper.getSendText());
        cbVoiceCall.setChecked(PreferencesHelper.getVoiceCall ());
        etPhone.setText(PreferencesHelper.getPhoneNum());

    }

    public void onClick(View view) {
        if (view.equals(cbText))
            PreferencesHelper.setSendText(cbText.isChecked());
        else if (view.equals(cbVoiceCall))
            PreferencesHelper.setVoiceCall(cbVoiceCall.isChecked());

    }

    public void onSaveClick(View view) {
        PreferencesHelper.setPhoneNum(etPhone.getText().toString());
    }


    public void onMinimizeClick(View view) {
        finish();
    }
}

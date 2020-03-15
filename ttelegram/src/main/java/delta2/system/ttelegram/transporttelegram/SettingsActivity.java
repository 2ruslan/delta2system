package delta2.system.ttelegram.transporttelegram;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.ttelegram.R;
import delta2.system.ttelegram.transporttelegram.Preferences.PreferencesHelper;


public class SettingsActivity extends AppCompatActivity {


    CheckBox cbText;
    CheckBox cbPhoto;
    CheckBox cbFile;
    CheckBox cbLocation;
    Button btClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegram_settings);

        cbText  = findViewById(R.id.cbText);
        cbFile  = findViewById(R.id.cbFile);
        cbPhoto = findViewById(R.id.cbPhoto);
        cbLocation = findViewById(R.id.cbLocation);
        btClose = findViewById(R.id.btClose);

        init();
    }

    private void init(){
        cbText.setChecked(PreferencesHelper.getSendText());
        cbFile.setChecked(PreferencesHelper.getSendFile());
        cbPhoto.setChecked(PreferencesHelper.getSendPhoto());
        cbLocation.setChecked(PreferencesHelper.getSendLocation());
    }

    public void onClick(View view) {
        if (view.equals(cbText))
            PreferencesHelper.setSendText(cbText.isChecked());
        else if (view.equals(cbFile))
            PreferencesHelper.setSendFile(cbFile.isChecked());
        else if (view.equals(cbPhoto))
            PreferencesHelper.setSendPhoto(cbPhoto.isChecked());
        else if (view.equals(cbLocation))

        PreferencesHelper.setSendLocation(cbLocation.isChecked());
    }

    public void onCloseClick(View view) {
        finish();
    }
}

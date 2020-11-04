package delta2.system.tdropbox.transportdropbox;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.tdropbox.R;
import delta2.system.tdropbox.transportdropbox.Preferences.PreferencesHelper;

public class SettingsActivity extends AppCompatActivity {

    CheckBox cbText;
    CheckBox cbPhoto;
    CheckBox cbFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox_settings);

        cbText  = findViewById(R.id.cbText);
        cbFile  = findViewById(R.id.cbFile);
        cbPhoto = findViewById(R.id.cbPhoto);

        init();
    }

    private void init(){
        cbText.setChecked(PreferencesHelper.getSendText());
        cbFile.setChecked(PreferencesHelper.getSendFile());
        cbPhoto.setChecked(PreferencesHelper.getSendPhoto());

    }

    public void onClick(View view) {
        if (view.equals(cbText))
            PreferencesHelper.setSendText(cbText.isChecked());
        else if (view.equals(cbFile))
            PreferencesHelper.setSendFile(cbFile.isChecked());
        else if (view.equals(cbPhoto))
            PreferencesHelper.setSendPhoto(cbPhoto.isChecked());
    }

    public void onMinimizeClick(View view) {
        finish();
    }
}

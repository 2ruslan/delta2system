package delta2.system.tftp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import delta2.system.tftp.Preferences.PreferencesHelper;

public class SettingsActivity extends AppCompatActivity {

    CheckBox cbText;
    CheckBox cbPhoto;
    CheckBox cbFile;

    EditText edFtpId;
    EditText edFtpUsr;
    EditText edFtpPass;
    EditText edFtpPort;
    EditText edFtpPath;

    Button btSaveFtpIp;
    Button btSaveFtpUsr;
    Button btSaveFtpPass;
    Button btSaveFtpPort;
    Button btSaveFtpPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cbText  = findViewById(R.id.cbText);
        cbFile  = findViewById(R.id.cbFile);
        cbPhoto = findViewById(R.id.cbPhoto);

        edFtpId = findViewById(R.id.etFtpIp);
        edFtpUsr = findViewById(R.id.etFtpUsr);
        edFtpPass = findViewById(R.id.etFtpPass);
        edFtpPort = findViewById(R.id.etFtpPort);
        edFtpPath = findViewById(R.id.etFtpPath);

        btSaveFtpIp   = findViewById(R.id.btSaveIp);
        btSaveFtpUsr  = findViewById(R.id.btSaveUsr);
        btSaveFtpPass = findViewById(R.id.btSavePass);
        btSaveFtpPort = findViewById(R.id.btSavePort);
        btSaveFtpPath = findViewById(R.id.btSavePath);

        init();
    }

    private void init(){
        cbText.setChecked(PreferencesHelper.getSendText());
        cbFile.setChecked(PreferencesHelper.getSendFile());
        cbPhoto.setChecked(PreferencesHelper.getSendPhoto());
        edFtpId.setText(PreferencesHelper.getFtpId());
        edFtpUsr.setText(PreferencesHelper.getFtpUsr());
        edFtpPass.setText(PreferencesHelper.getFtpPass());
        edFtpPort.setText(String.valueOf(PreferencesHelper.getFtpPort()));
        edFtpPath.setText(PreferencesHelper.getFtpPath());

    }

    public void onClick(View view) {
        if (view.equals(cbText))
            PreferencesHelper.setSendText(cbText.isChecked());
        else if (view.equals(cbFile))
            PreferencesHelper.setSendFile(cbFile.isChecked());
        else if (view.equals(cbPhoto))
            PreferencesHelper.setSendPhoto(cbPhoto.isChecked());

        else if (view.equals(btSaveFtpIp))
            PreferencesHelper.setFtpId(edFtpId.getText().toString());
        else if (view.equals(btSaveFtpPass))
            PreferencesHelper.setFtpPass(edFtpPass.getText().toString());
        else if (view.equals(btSaveFtpUsr))
            PreferencesHelper.setFtpUsr(edFtpUsr.getText().toString());
        else if (view.equals(btSaveFtpPort))
            PreferencesHelper.setFtpPort(Integer.valueOf(edFtpPort.getText().toString()));
        else if (view.equals(btSaveFtpPath))
            PreferencesHelper.setFtpPath(edFtpPath.getText().toString());
    }


    public void onMinimizeClick(View view) {
        finish();
    }
}

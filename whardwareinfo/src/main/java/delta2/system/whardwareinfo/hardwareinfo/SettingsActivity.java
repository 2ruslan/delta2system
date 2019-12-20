package delta2.system.whardwareinfo.hardwareinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import delta2.system.whardwareinfo.R;


public class SettingsActivity extends Activity {

    //CheckBox cbAutoStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardawareinfo_settings);

        //cbAutoStart = findViewById(R.id.cbAutoStart);
    }

    private void init(){
    //    cbAutoStart.setChecked(PreferencesHelper.getAutoStart());

    }

    /*
    public void onClick(View view) {
        if (view.equals(cbAutoStart))
            PreferencesHelper.setAutoStart(cbAutoStart.isChecked());

    }
*/

    public void onMinimizeClick(View view) {
        finish();
    }
}

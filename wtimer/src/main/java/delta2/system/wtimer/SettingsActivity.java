package delta2.system.wtimer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wtimer_settings);


        init();
    }

    private void init(){

    }

    public void onMinimizeClick(View view) {
        finish();
    }
}

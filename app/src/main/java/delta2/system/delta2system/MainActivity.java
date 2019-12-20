package delta2.system.delta2system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import delta2.system.common.Helper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();
    }



    ModuleManager moduleManager;
    void test(){
        moduleManager = new ModuleManager(this);
        moduleManager.init();
    }
}

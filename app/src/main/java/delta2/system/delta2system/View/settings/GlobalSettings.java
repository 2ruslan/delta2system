package delta2.system.delta2system.View.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;


import ch.qos.logback.classic.Level;
import delta2.system.common.Log.L;
import delta2.system.delta2system.MainService;
import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.PreferencesHelper;
import delta2.system.delta2system.R;
import delta2.system.delta2system.View.StarterApp;

public class GlobalSettings extends AppCompatActivity {

    ListView modulesList;
    ModuleAllAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_settings);

        MainService.stopApp();

        L.log.debug( "open");

        connectToAdapter();

        initControls();
    }

    private void initControls(){
        CheckBox auto = findViewById(R.id.cbAutostart);
        auto.setChecked(PreferencesHelper.getAutoStart());

        Spinner spinner = findViewById(R.id.spLogLevel);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.log_level_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String level = adapter.getItem(position).toString();
                PreferencesHelper.setLogLevel(level);
                L.setLogLevel(level);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinner.setAdapter(adapter);

        spinner.setSelection(adapter.getPosition(PreferencesHelper.getLogLevel()));
    }

    private void connectToAdapter(){
        L.log.debug( "connectToAdapter start");
        adapter = new ModuleAllAdapter(this, R.layout.list_item_select, ModuleManager.GetAllModules());
        modulesList = findViewById(R.id.modulsList);
        modulesList.setAdapter(adapter);
        L.log.debug( "connectToAdapter stop");
    }

    public void OnExitAndRun(View v){
        StarterApp.StartApp(this);

        finish();
    }
}

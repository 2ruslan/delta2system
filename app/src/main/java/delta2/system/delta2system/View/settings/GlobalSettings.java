package delta2.system.delta2system.View.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import delta2.system.common.Log.L;
import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.R;

public class GlobalSettings extends AppCompatActivity {
    private static final String _TAG = GlobalSettings.class.getName();

    ListView modulesList;
    ModuleAllAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_settings);

        L.log.debug(_TAG, "open");

        connectToAdapter();
    }


    private void connectToAdapter(){
        L.log.debug(_TAG, "connectToAdapter start");
        adapter = new ModuleAllAdapter(this, R.layout.list_item_select, ModuleManager.GetAllModules(this));
        modulesList = findViewById(R.id.modulsList);
        modulesList.setAdapter(adapter);
        L.log.debug(_TAG, "connectToAdapter stop");
    }
}

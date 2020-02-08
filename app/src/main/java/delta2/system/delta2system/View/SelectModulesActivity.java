package delta2.system.delta2system.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import delta2.system.delta2system.ModuleInfo;
import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.PreferencesHelper;
import delta2.system.delta2system.R;

public class SelectModulesActivity extends AppCompatActivity {

    public static final int _SELECT_TRANSPORT = 0;
    public static final int _SELECT_WORKER = 1;

    public static final String _SELECT_MODE = "_SELECT_MODE";

    private static ModuleManager moduleManager;

    private int mode;
    ListView modulesList;
    private ArrayList<ModuleInfo> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_modules);

        mode = getIntent().getIntExtra(_SELECT_MODE, _SELECT_TRANSPORT);

        connectToAdapter();
    }

    private void connectToAdapter(){
       // modules = mode == _SELECT_TRANSPORT ? moduleManager.GetTransportModules() : moduleManager.GetWorkerModules();
        ModuleSelectAdapter adapter = new ModuleSelectAdapter(this, R.layout.list_item_select, modules);
        modulesList = findViewById(R.id.modulsList);
        modulesList.setAdapter(adapter);
    }

    public static void init(ModuleManager m){
        moduleManager = m;
    }

    public static void destroy(){
        moduleManager = null;
    }


    public void OnSaveClick(View v){

        for (ModuleInfo mi : modules){
       //     PreferencesHelper.setIsActiveModule(mi.GetModuleId(), mi.isActive);
        }

        finish();
    }

    public void OnCancelClick(View v){
        finish();
    }

}

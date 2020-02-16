package delta2.system.delta2system.View.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import delta2.system.delta2system.INotifyChanged;
import delta2.system.delta2system.MainService;
import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.R;

public class MainActivity extends AppCompatActivity implements INotifyChanged {

    private static ModuleManager moduleManager;
    ListView modulesList;
    ModuleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectToAdapter();

        moduleManager.SetNotifyChanged(this);
    }

    public static void init(ModuleManager m){
        moduleManager = m;
    }

    public static void destroy(){
        moduleManager = null;
    }

    private void connectToAdapter(){
        adapter = new ModuleAdapter(this, R.layout.list_item, moduleManager.GetModules());
        modulesList = findViewById(R.id.modulsList);
        modulesList.setAdapter(adapter);
    }

    @Override
    public void OnNotifyChanged() {
        runOnUiThread(new Runnable() {
            public void run() {
                modulesList.invalidateViews();
            }
        });

    }

    public void OnExitClick(View v){
        MainService.stopApp();

        finish();
    }
}

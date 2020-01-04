package delta2.system.delta2system.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.R;


public class MainActivity extends AppCompatActivity {

    private static ModuleManager moduleManager;
    ListView modulesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectToAdapter();

    }

    public static void init(ModuleManager m){
        moduleManager = m;
    }

    public static void destroy(){
        moduleManager = null;
    }

    private void connectToAdapter(){
        ModuleAdapter adapter = new ModuleAdapter(this, R.layout.list_item, moduleManager.GetAllModules());
        modulesList = findViewById(R.id.modulsList);
        modulesList.setAdapter(adapter);
    }

    public void OnTransportClick(View v){
        SelectModulesActivity.init(moduleManager);
        Intent i = new Intent(this, SelectModulesActivity.class);
        i.putExtra(SelectModulesActivity._SELECT_MODE, SelectModulesActivity._SELECT_TRANSPORT);
        startActivityForResult(i, 0);
    }

    public void OnWorkerClick(View v){
        SelectModulesActivity.init(moduleManager);
        Intent i = new Intent(this, SelectModulesActivity.class);
        i.putExtra(SelectModulesActivity._SELECT_MODE, SelectModulesActivity._SELECT_WORKER);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SelectModulesActivity.destroy();
        moduleManager.Reinit();
    }

}

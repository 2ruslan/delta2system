package delta2.system.delta2system.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.R;


public class MainActivity extends AppCompatActivity {

    private static ModuleManager moduleManager;
    ListView modulsList;

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
        modulsList = findViewById(R.id.modulsList);
        modulsList.setAdapter(adapter);
    }
}

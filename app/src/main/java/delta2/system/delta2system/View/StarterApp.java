package delta2.system.delta2system.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

import delta2.system.common.Constants;
import delta2.system.common.Helper;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.delta2system.MainService;
import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.R;
import delta2.system.delta2system.View.main.MainActivity;
import delta2.system.delta2system.View.settings.GlobalSettings;

public class StarterApp extends Activity {

    public final static String _SHOW_SETTINGS = "_SHOW_SETTINGS";

    public static void StartApp(Context c){
        Intent i = new Intent(c, StarterApp.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        c.startActivity(i);
    }

    public static void StartSettings(Context c){
        Intent i = new Intent(c, StarterApp.class);
        i.putExtra(_SHOW_SETTINGS, true);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askAndroid10Perm();

        setContentView(R.layout.activity_starter_app);

        Helper.setWorkDir(this.getFilesDir());

        this.startService(new Intent(this, MainService.class));

     //   ModuleManager.init(this);

    //    go (!ModuleManager.CheckExistsActiveModule() || getIntent().getBooleanExtra(_SHOW_SETTINGS, false));
    }

    private void go(boolean isShowSettings){
        if (isShowSettings){

            MainService.stopApp();

            Intent i = new Intent(this, GlobalSettings.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            this.startService(new Intent(this, MainService.class));
        }
    }

    private void askAndroid10Perm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                            | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
            startActivityForResult(intent, 0);
        }
    }

}
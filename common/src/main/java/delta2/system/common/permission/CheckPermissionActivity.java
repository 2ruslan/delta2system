package delta2.system.common.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

import delta2.system.common.Constants;
import delta2.system.common.R;
import delta2.system.common.interfaces.IAcnivityCallback;

public class CheckPermissionActivity extends Activity {


    public final static int OVERLAY_PERMISSION_REQ_CODE = 1;

    private ArrayList<String> allPermission;
    private static IAcnivityCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);

        allPermission = getIntent().getStringArrayListExtra(Constants._ALL_PERMISSION);

        checkAllPermission();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void destroy(){
        callback = null;
    }

    public void checkAllPermission()
    {
        if (allPermission != null && !allPermission.isEmpty()) {
            do {
                String permission = allPermission.get(0);
                allPermission.remove(0);

                if (Settings.ACTION_MANAGE_OVERLAY_PERMISSION.equals(permission)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(this)) {
                            String p = getPackageName();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + p));
                            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                            return;
                        }
                    }
                }
                else {
                    int res = this.checkCallingOrSelfPermission(permission);
                    if (res != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
                        return;
                    }
                }
            } while (!allPermission.isEmpty());
        }

        if (allPermission == null || allPermission.isEmpty())
            OnFinish(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        checkAllPermission();
                    else
                        OnFinish(false);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)){
                checkAllPermission();
            }
            else{
                OnFinish(false);
            }
        }
    }

    private void OnFinish(boolean isOk){
        callback.OnActivityCallback(new Intent().putExtra(Constants._ALL_PERMISSION, isOk));
        finish();
    }

    public static void setActivityCallback(IAcnivityCallback c){
        callback = c;
    }
}
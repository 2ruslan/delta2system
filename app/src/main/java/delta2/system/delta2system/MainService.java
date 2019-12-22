package delta2.system.delta2system;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import delta2.system.delta2system.View.MainActivity;

public class MainService extends Service implements IAppCompleteInit{
    ModuleManager moduleManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        PreferencesHelper.init(this);

        moduleManager = new ModuleManager(this, this);
        moduleManager.init();
    }

    @Override
    public void OnAppCompleteInit() {
        startForeground(R.drawable.ic_notify_proc, "delta2system", 1100);

        MainActivity.init(moduleManager);
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        MainActivity.destroy();
        moduleManager.destroy();
    }

    protected  void startForeground(int ico, String title, int notifyId) {

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(ico)
                .setContentTitle(title)
                .setContentText("")
                .setOnlyAlertOnce(true)
                .setOngoing(true);
        Notification notification;

        notification = builder.build();

        notification.contentIntent = PendingIntent.getActivity(this,
                0, new Intent(getApplicationContext(), MainService.class)
                , PendingIntent.FLAG_UPDATE_CURRENT);


        startForeground(notifyId, notification);
    }


}

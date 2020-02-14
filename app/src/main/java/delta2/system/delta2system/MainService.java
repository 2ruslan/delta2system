package delta2.system.delta2system;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import delta2.system.delta2system.View.MainActivity;

public class MainService extends Service {
    ModuleManager moduleManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        PreferencesHelper.init(this);

        moduleManager = new ModuleManager(this);

        startForeground(R.drawable.ic_notify_proc, "delta2system", 1100);

        MainActivity.init(moduleManager);
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        moduleManager.init();
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setChannelId(createNotificationChannel("my_service", "My Background Service"));
        }

        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        else{
            notification = builder.getNotification();
        }


        notification.contentIntent = PendingIntent.getActivity(this,
                0, new Intent(getApplicationContext(), MainService.class)
                , PendingIntent.FLAG_UPDATE_CURRENT);


        startForeground(notifyId, notification);
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }
}

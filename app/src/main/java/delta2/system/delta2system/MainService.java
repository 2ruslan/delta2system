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
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import java.util.Calendar;

import delta2.system.common.Helper;
import delta2.system.delta2system.View.main.MainActivity;

public class MainService extends Service {
    ModuleManager moduleManager;
    static MainService instance;

    PowerManager.WakeLock wakeLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        instance = this;
        startTime = Calendar.getInstance().getTimeInMillis();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "d2s::MainService");
        wakeLock.acquire();

        startForeground(R.drawable.ic_notify_proc, "delta2system", 1100);

        moduleManager = new ModuleManager(this);

        showMainWindow();

        moduleManager.init();
    }

    @Override
    public void onDestroy() {
        MainActivity.destroy();
        wakeLock.release();
        moduleManager.destroy();
    }

    public static void stopApp(){
        if (instance != null)
            instance.stopSelf();
    }

    private void showMainWindow(){
        MainActivity.init(moduleManager);

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void startForeground(int ico, String title, int notifyId) {


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

    private static long startTime;
    public static String getWorkingTime(){
        long uptime = Calendar.getInstance().getTimeInMillis() - startTime;

        return Helper.getWorkingTime(instance, uptime);
    }
}
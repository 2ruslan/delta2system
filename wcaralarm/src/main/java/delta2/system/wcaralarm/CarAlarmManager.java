package delta2.system.wcaralarm;

import android.content.Context;

import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wcaralarm.Accelerometer.AccelerationManager;
import delta2.system.wcaralarm.GPS.GpsManager;

public class CarAlarmManager {

    private AccelerationManager accelerationManager;
    private GpsManager gpsManager;


    public CarAlarmManager(Context context, IRequestSendMessage msg){
        accelerationManager = new AccelerationManager(context, msg);
        gpsManager = new GpsManager(context, msg);
    }

    public void start(){
        accelerationManager.start();
        gpsManager.start();
    }

    public void stop(){
        accelerationManager.stop();
        gpsManager.stop();
    }

    public void destroy(){
        if (accelerationManager != null)
            accelerationManager.onDestroy();
        if (gpsManager != null)
            gpsManager.onDestroy();

    }

}

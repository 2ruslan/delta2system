package delta2.system.delta2system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import delta2.system.delta2system.View.StarterApp;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferencesHelper.init(context);
        if(PreferencesHelper.getAutoStart() ) {
            context.startActivity(new Intent(context, StarterApp.class));
        }
    }
}
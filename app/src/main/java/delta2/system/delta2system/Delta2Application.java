package delta2.system.delta2system;

import android.app.Application;

import androidx.multidex.MultiDex;

import delta2.system.common.FileStructure;
import delta2.system.common.Log.L;
import delta2.system.common.preferences.PreferenceValue;


public class Delta2Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        FileStructure.init();

        PreferencesHelper.SetContext(this);

        L.configureLogback(PreferencesHelper.getLogLevel());

        Thread.setDefaultUncaughtExceptionHandler(new CustomExHandler());
    }

    public class CustomExHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            L.log.error("", ex);
        }
    }
}




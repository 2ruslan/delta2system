package delta2.system.delta2system;

import android.app.Application;

import androidx.multidex.MultiDex;

import delta2.system.common.FileStructure;
import delta2.system.common.Log.L;
import delta2.system.common.preferences.PreferenceValue;
import delta2.system.framework.common.Log;
import delta2.system.framework.interfaces.ILogger;


public class Delta2Application extends Application {
    protected ILogger logger = Log.Instance();

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        FileStructure.init();

        PreferencesHelper.SetContext(this);

        Log.Configure(PreferencesHelper.getLogLevel(), FileStructure.getWorkFilesDir(this));

        Thread.setDefaultUncaughtExceptionHandler(new CustomExHandler());
    }

    public class CustomExHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            logger.error(ex);
        }
    }
}




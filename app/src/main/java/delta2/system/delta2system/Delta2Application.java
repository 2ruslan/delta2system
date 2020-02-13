package delta2.system.delta2system;

import android.app.Application;

import delta2.system.common.FileStructure;
import delta2.system.common.Log.L;


public class Delta2Application extends Application {

    private static final  String _TAG = Delta2Application.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        FileStructure.init();

        L.configureLogback();

        Thread.setDefaultUncaughtExceptionHandler(new CustomExHandler());
    }

    public class CustomExHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            L.log.error(_TAG, ex);
        }
    }


}




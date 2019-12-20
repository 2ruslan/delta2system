package delta2.system.delta2system;

import android.app.Application;

import delta2.system.common.Helper;

public class Delta2Application extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExHandler());
    }

    public class CustomExHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Helper.Ex2Log(ex);
        }
    }
}




package delta2.system.common;

import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {
    private final Logger logger = Logger.getLogger("delta2system");
    private FileHandler fh = null;

    private String logPath;

    public String GetLogPath(){
        return logPath;
    }

    public AppLogger(Level level){
        logPath = getLogPath();

        try {
            fh = new FileHandler(logPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        logger.setLevel(level);
    }


    private String getLogPath(){
        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");

        String dir;
        if (Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState()))
            dir =  String.format("%s/", Environment.getExternalStorageDirectory () );
        else
            dir =  String.format("%s/", Environment.getDataDirectory() );

        return dir + "/delta2system/" + format.format(Calendar.getInstance().getTime()) + ".log";
    }
}

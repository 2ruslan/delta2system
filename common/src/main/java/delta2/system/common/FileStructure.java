package delta2.system.common;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import delta2.system.common.Log.L;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class FileStructure {
    private static final int _OLD_FILES_QNT = 300;
    private static final String _D2S_DIR = "/delta2system/";

    final static SimpleDateFormat sdfFileShort = new SimpleDateFormat(".yyyy_MM_dd__HH_mm_ss.SSS");

    private static String filessPath;

    private static String logPath;
    public static String GetLogPathDir(){
        return logPath;
    }

    public static void init(){
        String baseDir = (Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState())) ?
                            Environment.getExternalStorageDirectory().toString()
                            : Environment.getDataDirectory().toString();
        logPath = baseDir + _D2S_DIR + "logs/";
        filessPath = baseDir + _D2S_DIR + "files/";

        CreateIfNeed(baseDir + _D2S_DIR);
        CreateIfNeed(logPath);
        CreateIfNeed(filessPath);
        deleteAllFiles();
    }


    public static String getFilePath(String prefix, String ext){
        deleteOldFiles();
        return filessPath + prefix + sdfFileShort.format(Calendar.getInstance().getTime()) + "." + ext;
    }

    public static String getWorkFilesDir(Context c){
        try {
            return c.getFilesDir().getCanonicalPath();
        }catch (Exception e){
            return filessPath;
        }
    }

    private static void CreateIfNeed(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            if (dir.mkdirs()) {
                dir.setReadable(true, false);
                dir.setWritable(true, false);
            }
        }

        File nomediaDir = new File(String.format("%s/.nomedia", path));
        if(!nomediaDir.exists())
            try {
                nomediaDir.createNewFile();
            }catch (Exception e) {
                L.log.error("", e);
            }
    }

    private static void deleteOldFiles(){
        try {
            File dir = new File(filessPath);
            if (dir.listFiles().length > _OLD_FILES_QNT) {
                final File[] sortedByDate = dir.listFiles();

                if (sortedByDate != null && sortedByDate.length > 1) {
                    Arrays.sort(sortedByDate, new Comparator<File>() {
                        @Override
                        public int compare(File object1, File object2) {
                            return compareLastModified(object1.lastModified(), object2.lastModified());
                        }
                    });
                }

                for (int i = 0; i < 20; i++) {
                    L.log.debug("Deleted file : %s", sortedByDate[i].getName());
                    sortedByDate[i].delete();
                }
            }
        }
        catch (Exception e)
        {
            L.log.error(e.getMessage());
            deleteAllFiles();
        }
    }

    private static void deleteAllFiles(){
        try {
            File dir = new File(filessPath);
            final File[] files = dir.listFiles();
            for (int i = 0; i < 20; i++) {
                L.log.debug("Deleted file : %s", files[i].getName());
                files[i].delete();
            }
        } catch (Exception e)
        {
            L.log.error(e.getMessage());
        }
    }

    private static int compareLastModified(long first, long second) {

        if ((first - second) < .000001)
            return 0;
        if (first > second)
            return -1;
        else if (first < second)
            return 1;
        return 0;
    }
}

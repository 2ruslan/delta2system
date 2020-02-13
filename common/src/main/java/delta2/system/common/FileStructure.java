package delta2.system.common;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

public class FileStructure {
    private static final int _OLD_FILES_QNT = 120;
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
    }


    public static String getFilePath(String prefix, String ext){
        deleteOldFiles();
        return filessPath + prefix + sdfFileShort.format(Calendar.getInstance().getTime()) + "." + ext;
    }


    private static void CreateIfNeed(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdirs();
            dir.setReadable(true, false);
            dir.setWritable(true, false);
        }
    }

    private static void deleteOldFiles(){
        File dir = new File(filessPath);
        if(dir.listFiles().length > _OLD_FILES_QNT){
            final File[] sortedByDate = dir.listFiles();

            if (sortedByDate != null && sortedByDate.length > 1) {
                Arrays.sort(sortedByDate, new Comparator<File>() {
                    @Override
                    public int compare(File object1, File object2) {
                        return (int) ((object1.lastModified() < object2.lastModified()) ? object1.lastModified(): object2.lastModified());
                    }
                });
            }

            for (int i=0; i < 20; i++ )
                sortedByDate[i].delete();
        }
    }
}

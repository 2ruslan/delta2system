package delta2.system.wmotiondetector.motiondetector.Detector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import delta2.system.common.Helper;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;

public class SendPhoto {



    //region savedir
    //static final String _DIR_TMP = "/MotionDetector/";
    static final String _DIR_TMP = "/images/";
    static File saveDir = null;
    static public File getDir(Context c){
        if(saveDir == null) {
            saveDir = new File(String.format("%s%s", Helper.getWorkDir(), _DIR_TMP));
            //saveDir = new File(String.format("%s%s", Environment.getExternalStorageDirectory(), _DIR_TMP));

            if (!saveDir.exists()) {
                saveDir.mkdirs();
                saveDir.setReadable(true, false);
                saveDir.setWritable(true, false);

            }

            for (File child : saveDir.listFiles())
                child.delete();

        }

        if(saveDir.listFiles().length > 120){
            final File[] sortedByDate = saveDir.listFiles();

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



        return saveDir;
    }
    //endregion savedir


    final static SimpleDateFormat sdfFileShort = new SimpleDateFormat("yyy_MM_dd__HH_mm_ss.SSS");
    public static String getNowDTFile(){
        return sdfFileShort.format(Calendar.getInstance().getTime());
    }

    public static void Send(Context contest, byte[] jpg, String info){

        try {
            File saveDir =  getDir(contest);

            Helper.Log( "photo.send ", "start");
            String fileName = String.format( "%s/%s.jpg", saveDir.getAbsolutePath(), getNowDTFile());
            FileOutputStream os = new FileOutputStream(fileName);
            os.write(jpg);
            os.close();
            Helper.Log( "photo.send ", "file ok");


            /**/
            File requestFile = new File(fileName);


            try {
                Uri fileUrioUT = FileProvider.getUriForFile(
                        contest,
                        "delta2.system.motiondetector.fileprovider",
                        requestFile);


                    List<PackageInfo> packageInfo = contest.getPackageManager().getInstalledPackages(0);
                    for (PackageInfo p : packageInfo) {
                        String packageName = p.packageName;
                        contest.grantUriPermission(packageName, fileUrioUT, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                MediatorMD.sendPhoto( fileUrioUT.toString() , Helper.getNowDT() + " " + info);

            } catch (IllegalArgumentException e) {
                Helper.Log("File Selector",
                        "The selected file can't be shared: " + requestFile.toString());
            }
            /**/


            Helper.Log( "photo.send ", "2transport");

        } catch (Exception e) {
            Helper.Ex2Log(e);
        }
    }

}

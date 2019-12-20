package delta2.system.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.WindowManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

    //region workdir
    static File _WorkDir;
    static File nomediaDir;

    public static File getWorkDir(){
        return  _WorkDir;
    }
    public static void setWorkDir(File f){
        _WorkDir = f;

        if(nomediaDir == null) {
            nomediaDir = new File(String.format("%s/.nomedia", _WorkDir));
            if(!nomediaDir.exists())
                try {
                    nomediaDir.createNewFile();
                }catch (Exception e){
                    Helper.Ex2Log(e);
                }
        }
    }

    public static String getWorkDirpath(){
        try {
            return getWorkDir().getCanonicalPath();
        }catch (Exception e){
            return "";
        }
    }

    //endregion workdir

    //region dt
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getNowDT(){
        return sdf.format(Calendar.getInstance().getTime());
    }

    final static SimpleDateFormat sdfFile = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
    public static String getNowDTFile(){
        return sdfFile.format(Calendar.getInstance().getTime());
    }

    //endregion dt

    //region log
    private static File _logFile = null;

    private static boolean _isNeedLog = false;
    private static boolean _isNeedLogInit = false;
    private static boolean getIsNeedLog(){
        if (!_isNeedLogInit)
            try
            {
                _isNeedLog = (new File( Environment.getExternalStorageDirectory () + "/" + "delta.need.log")).exists() ;
            }catch (Exception e)
            {

            }
        _isNeedLogInit = true;

        return _isNeedLog;
    }

    public static void Log(String tag, String msg){
        Log( tag, msg, false);
    }

    public static void Log(String tag, String msg, boolean isMandatory){

        if (isMandatory || getIsNeedLog())

            try{
                String timeLog = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").format(Calendar.getInstance().getTime());

                if(_logFile == null){
                    String dir;
                    if (Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState()))
                        dir =  String.format("%s/", Environment.getExternalStorageDirectory () );
                    else
                        dir =  String.format("%s/", getWorkDir() );

                    _logFile = new File(dir, "delta2_main.log.txt");

                    if (_logFile.exists()) {
                        _logFile.renameTo(new File(dir, String.format("delta.log_%s.txt",
                                    new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(Calendar.getInstance().getTime()) )));

                    }
                    _logFile.createNewFile();
                }

                BufferedWriter bw = new BufferedWriter(new FileWriter(_logFile, true));
                bw.append( timeLog +"\t" + tag + " : \t" + msg + "\n\r");
                bw.close();

            } catch (Exception e) {

            }

    }

    public static void Ex2Log(Throwable ex){
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            Log("Exception", sw.toString(), true);

        }
        catch (Exception e)
        {

        }
    }


    //endregion log


    public static String getWorkingTime(Context c, long time ) {
        long ssInDay = 86400;
        long hhInDay = 3600;
        long mmInDay = 60;

        try {

            long wts = time / 1000; // (Calendar.getInstance().getTimeInMillis() - _startTimeMls) / 1000;

            long dd = wts / ssInDay;
            wts -= (dd * ssInDay);

            long hh = wts / hhInDay;
            wts -= (hh * hhInDay);

            long mm = wts / mmInDay;
            wts -= (mm * mmInDay);

            long ss = wts;

            StringBuilder res = new StringBuilder();
          //  if (dd > 0)
          //      res.append(String.format("%s%s ", dd, c.getString(R.string.working_time_d)));
          //  if (hh > 0 || dd > 0)
          //      res.append(String.format("%s%s ", (hh < 10 ? "0" : "") + String.valueOf(hh), c.getString(R.string.working_time_h)));
          //  if (mm > 0 || hh > 0 || dd > 0)
          //      res.append(String.format("%s%s ", (mm < 10 ? "0" : "") + String.valueOf(mm), c.getString(R.string.working_time_m)));

          //  res.append(String.format("%s%s ", (ss < 10 ? "0" : "") + String.valueOf(ss), c.getString(R.string.working_time_s)));

            return res.toString();
        }catch (Exception ex){
            return "";
        }
    }

    public static void showDialog(Context c, String capt, String msg) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(c)
                    .setTitle(capt)
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog dialog = b.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();


        }catch (Exception e){
            Helper.Ex2Log(e);
        }
    }

}

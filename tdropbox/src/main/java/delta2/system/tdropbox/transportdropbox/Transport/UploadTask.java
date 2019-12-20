package delta2.system.tdropbox.transportdropbox.Transport;

import android.content.Context;
import android.os.AsyncTask;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import delta2.system.tdropbox.transportdropbox.Preferences.PreferencesHelper;

public class UploadTask extends AsyncTask {

    private DbxClientV2 dbxClient;
    private File file;
    private Context context;

    UploadTask(DbxClientV2 dbxClient, File file, Context context) {
        this.dbxClient = dbxClient;
        this.file = file;
        this.context = context;
    }

    final static SimpleDateFormat sdfFile = new SimpleDateFormat("yyyy/MM/dd/HH");
    public static String getNowFolder(){
        return sdfFile.format(Calendar.getInstance().getTime());
    }

    final static SimpleDateFormat sdfFileShort = new SimpleDateFormat("HH_mm_ss.SSS");
    public static String getNowDTFile(){
        return sdfFileShort.format(Calendar.getInstance().getTime());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {

            InputStream inputStream = new FileInputStream(file);
            dbxClient.files().uploadBuilder(
                    (PreferencesHelper.getDeviceName().equals("") ? ""
                            : "/" + PreferencesHelper.getDeviceName()) +
                    "/" + getNowFolder() + "/"
                    + getNowDTFile() + file.getName().substring(file.getName().lastIndexOf("."))  )
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);


        file.delete();

    }
}
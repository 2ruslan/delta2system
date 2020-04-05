package delta2.system.tftp.Transport;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

import delta2.system.tftp.Preferences.PreferencesHelper;

public class UploadTask extends AsyncTask {

    private String filename;
    private Context context;

    UploadTask( String f, Context context) {
        this.filename = f;
        this.context = context;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        try {

            FTPClient con = new FTPClient();
            con.setDefaultPort(PreferencesHelper.getFtpPort());
            con.connect( PreferencesHelper.getFtpId());


            if (con.login(PreferencesHelper.getFtpUsr(), PreferencesHelper.getFtpPass()))
            {
                con.enterLocalPassiveMode();
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = filename;

                File f = new File(data);

                String hp= PreferencesHelper.getFtpPath();
                String ftpPath = hp.length() > 0 ? hp + "/" + f.getName() : f.getName();

                FileInputStream in = new FileInputStream(f);
                con.storeFile(  ftpPath, in);
                in.close();

                con.logout();
                con.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}
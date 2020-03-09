package delta2.system.tftp.Transport;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import delta2.system.tftp.Preferences.PreferencesHelper;
import delta2.system.transportftp.Helper;
import delta2.system.transportftp.Mediator.ITransport;
import delta2.system.transportftp.Preferences.PreferencesHelper;

public class FtpTransport implements ITransport {

    Context _context;

    public FtpTransport(Context c){
        _context = c;
    }


    public void init(Context context) {
        _context = context;
    }

    public void close() {
        _context = null;

    }


    @Override
    public void sendTxt( String msg) {

        if (!PreferencesHelper.getSendText())
            return;

        try {
            String filename = "msg_" + Helper.getNowDTFile() + ".txt";

            final File file = new File(Helper.getWorkDir(), filename);

            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(msg);

            myOutWriter.close();

            fOut.flush();
            fOut.close();


            send2ftp(file.getAbsolutePath());
        }
        catch (Exception e) {
            Helper.Ex2Log(e);
        }
    }

    @Override
    public void sendPhoto(String file, String caption) {
        if (!PreferencesHelper.getSendPhoto())
            return;


        /**/
        Uri returnUri = Uri.parse(file);

        String fn =returnUri.getPath();
        int cut = fn.lastIndexOf('/');
        if (cut != -1) {
            fn = fn.substring(cut + 1);
        }


        String fileName = String.format( "%s/%s.jpg", Helper.getWorkDir().getAbsolutePath(), fn );
        File sendingFile = new File(  fileName );

        try {
            ParcelFileDescriptor sendFile = _context.getContentResolver().openFileDescriptor(returnUri, "r");
            FileDescriptor fd = sendFile.getFileDescriptor();



            InputStream fileStream = new FileInputStream(fd);
            OutputStream localFile = new FileOutputStream( sendingFile);

            byte[] buffer = new byte[1024];
            int length;

            while((length = fileStream.read(buffer)) > 0)
            {
                localFile.write(buffer, 0, length);
            }

            localFile.flush();
            fileStream.close();
            localFile.close();

            send2ftp(sendingFile.getAbsolutePath());

        }catch (Exception e)
        {
            Helper.Ex2Log(e);
        }

        ////////


    }



    @Override
    public void sendFile(String file) {
        if (!PreferencesHelper.getSendFile())
            return;

        File f = new File(file);


    }


    private void send2ftp(String filename){

        new UploadTask(filename, _context).execute();

    }


}

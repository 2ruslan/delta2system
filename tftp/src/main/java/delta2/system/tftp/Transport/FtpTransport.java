package delta2.system.tftp.Transport;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import delta2.system.common.FileStructure;
import delta2.system.common.Log.L;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.tftp.Preferences.PreferencesHelper;

public class FtpTransport {

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

    public void SendMessage(IMessage msg){

    }

    public void sendTxt( String msg) {

        if (!PreferencesHelper.getSendText())
            return;

        try {
            String filename = FileStructure.getFilePath("msg_", "txt");

            final File file = new File(filename);

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
            L.log.error("", e);
        }
    }

    public void sendPhoto(String file, String caption) {
        if (!PreferencesHelper.getSendPhoto())
            return;

        try{
            send2ftp(file);

        }catch (Exception e)
        {
            L.log.error("", e);
        }
    }

    public void sendFile(String file) {
        if (!PreferencesHelper.getSendFile())
            return;

        try{
            send2ftp(file);

        }catch (Exception e)
        {
            L.log.error("", e);
        }
    }


    private void send2ftp(String filename){

        new UploadTask(filename, _context).execute();
    }
}

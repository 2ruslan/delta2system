package delta2.system.tmail.Transport;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import delta2.system.common.FileStructure;
import delta2.system.common.Log.L;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.messages.MessageFile;
import delta2.system.common.messages.MessagePhoto;
import delta2.system.common.messages.MessageText;
import delta2.system.tmail.Preferences.PreferencesHelper;

// send
// https://gist.github.com/wfng92/f131ea2d96d0d90338bb496aac97cfdc
// https://medium.com/better-programming/how-to-send-an-email-with-attachments-in-android-b2d75123f7e7

//read
// https://www.tutorialspoint.com/javamail_api/javamail_api_checking_emails.htm

public class MailTransport {

    private Context context;

    public MailTransport(Context c){
        context = c;
    }

    public void SendMessage(IMessage msg){
        if (msg instanceof MessageText)
            sendTxt( ((MessageText)msg).GetText() );
        else if (msg instanceof MessageFile)
            sendFile( ((MessageFile)msg).GetFile() );
        else if (msg instanceof MessagePhoto) {
            MessagePhoto mp = (MessagePhoto)msg;
            sendPhoto(mp.GetFile(), mp.GetCaption());
        }

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

        //new UploadTask(filename, _context).execute();
    }

}

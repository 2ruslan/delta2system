package delta2.system.tdropbox.transportdropbox.Transport;

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

import delta2.system.common.Helper;
import delta2.system.common.interfaces.IInit;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.ISendMessage;
import delta2.system.common.messages.MessageFile;
import delta2.system.common.messages.MessagePhoto;
import delta2.system.common.messages.MessageText;
import delta2.system.tdropbox.transportdropbox.Preferences.PreferencesHelper;

public class DropBoxTransport implements ISendMessage, IInit {

    Context context;

    public DropBoxTransport(Context c){
        context = c;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        context = null;
    }

    @Override
    public void SendMessage(IMessage msg) {
        if (msg instanceof MessageText){
            sendTxt((MessageText) msg);
        }else if (msg instanceof MessagePhoto){
            sendPhoto((MessagePhoto)msg);
        }else if (msg instanceof MessageFile){
            sendFile((MessageFile)msg);
        }

    }

    public void sendTxt( MessageText msg) {

        if (!PreferencesHelper.getSendText())
            return;

        try {
            String filename = "msg_" + Helper.getNowDTFile() + ".txt";

            final File file = new File(Helper.getWorkDir(), filename);

            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(msg.GetText());

            myOutWriter.close();

            fOut.flush();
            fOut.close();

            new UploadTask(DropboxClient.getClient(PreferencesHelper.getToken()), file, context).execute();

        }
        catch (Exception e) {
            Helper.Ex2Log(e);
        }
    }

    public void sendPhoto(MessagePhoto msg) {
        if (!PreferencesHelper.getSendPhoto())
            return;

        /**/
        Uri returnUri = Uri.parse(msg.GetFile());

        String fileName = String.format( "%s/%d.jpg", context.getFilesDir().getAbsolutePath(), System.currentTimeMillis());
        File sendingFile = new File(  fileName );

        try {
            ParcelFileDescriptor sendFile = context.getContentResolver().openFileDescriptor(returnUri, "r");
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

        }catch (Exception e)
        {
            Helper.Ex2Log(e);
        }

        ////////

        File f = new File(fileName);
        new UploadTask(DropboxClient.getClient(PreferencesHelper.getToken()), f, context).execute();

    }

    public void sendFile(MessageFile msg) {
        if (!PreferencesHelper.getSendFile())
            return;

        File f = new File(msg.GetFile());
        new UploadTask(DropboxClient.getClient(PreferencesHelper.getToken()), f, context).execute();

    }



}

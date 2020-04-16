package delta2.system.tsignal;

import android.content.Context;

import delta2.system.common.Log.L;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.messages.MessageFile;
import delta2.system.common.messages.MessagePhoto;
import delta2.system.common.messages.MessageText;
import delta2.system.tsignal.Preferences.PreferencesHelper;

public class SignalTransport {

//https://github.com/signalapp/libsignal-service-java

//https://github.com/signalapp/libsignal-protocol-java

    Context _context;

    public SignalTransport(Context c){
        _context = c;
    }

/*
    private void createKeys(){
        IdentityKeyPair identityKey        = KeyHelper.generateIdentityKeyPair();
        List<PreKeyRecord> oneTimePreKeys     = KeyHelper.generatePreKeys(0, 100);
        PreKeyRecord       lastResortKey      = KeyHelper.generateLastResortPreKey();
        SignedPreKeyRecord signedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKey, signedPreKeyId);
    }


    private void register(){
        private final String     URL         = "https://my.signal.server.com";
        private final TrustStore TRUST_STORE = new MyTrustStoreImpl();
        private final String     USERNAME    = "+14151231234";
        private final String     PASSWORD    = generateRandomPassword();
        private final String     USER_AGENT  = "[FILL_IN]";

        SignalServiceAccountManager accountManager = new SignalServiceAccountManager(URL, TRUST_STORE,
                USERNAME, PASSWORD, USER_AGENT);

        accountManager.requestSmsVerificationCode();
        accountManager.verifyAccountWithCode(receivedSmsVerificationCode, generateRandomSignalingKey(),
                generateRandomInstallId(), false);
        accountManager.setGcmId(Optional.of(GoogleCloudMessaging.getInstance(this).register(REGISTRATION_ID)));
        accountManager.setPreKeys(identityKey.getPublicKey(), lastResortKey, signedPreKeyRecord, oneTimePreKeys);
    }
*/
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
      /*      SignalServiceMessageSender messageSender = new SignalServiceMessageSender(URL, TRUST_STORE, USERNAME, PASSWORD,
                    new MySignalProtocolStore(),
                    USER_AGENT, Optional.absent());

            messageSender.sendMessage(new SignalServiceAddress("+14159998888"),
                    SignalServiceDataMessage.newBuilder()
                            .withBody("Hello, world!")
                            .build());

        */
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

       // new UploadTask(filename, _context).execute();
    }
}

package delta2.system.ttelegram.transporttelegram.transport;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.framework.common.FileStructure;
import delta2.system.framework.common.Log;
import delta2.system.framework.common.MessageFactory;
import delta2.system.framework.interfaces.ILogger;
import delta2.system.framework.interfaces.IMessage;
import delta2.system.framework.interfaces.IMessageReceiver;
import delta2.system.framework.interfaces.IMessageSend;
import delta2.system.framework.interfaces.IMessageSendText;
import delta2.system.framework.interfaces.ITransport;
import delta2.system.ttelegram.BuildConfig;
import delta2.system.ttelegram.Constants;
import delta2.system.ttelegram.IAcnivityCallback;
import delta2.system.ttelegram.transporttelegram.LoginActivity;
import delta2.system.ttelegram.transporttelegram.preferences.PreferencesHelper;
import delta2.system.ttelegram.transporttelegram.preferences.TelegramPreferences;

public class TelegramTransport implements Client.ResultHandler, Client.ExceptionHandler, ITransport {
    private static final int _ONLINE_INTERVAL = 5 * 60 *1000;
    private static final int _DELETE_HISTORY_INTERVAL = 6 * 60 * 60 *1000;

    protected ILogger logger = Log.Instance();

    Context _context;


    Timer _Timer;
    OnlineTimertask _TimerTask;

    Timer _TimerDelHIst;
    DeleteHistTask _TimerTaskDelHist;

    Client _client;

    boolean isWaitCode = false;
    boolean isWaitPhone = false;
    boolean isWaitPass = false;
    String passHint;

    long locationMsgId = 0;

    IAcnivityCallback callback;

    IMessageReceiver receiver;

    public TelegramTransport (Context c, IMessageReceiver r){
        _context = c;
        receiver = r;

        _client = Client.create(this, this, this);
    }

    public void setAcnivityCallback(IAcnivityCallback c){
        callback = c;
    };


    @Override
    public void onResult(TdApi.Object object) {
        logger.trace(object.toString());


        if (object instanceof TdApi.UpdateAuthorizationState) {
            TdApi.UpdateAuthorizationState state = (TdApi.UpdateAuthorizationState) object;
            onResult(state.authorizationState);
            return;
        }

        //if(object instanceof TdApi.Un)

        if (object instanceof TdApi.AuthorizationStateWaitPhoneNumber && !isWaitPhone) {
            isWaitPhone = true;
            PreferencesHelper.SetPhoneNum("");
            register();

        }
        else if (isWaitPhone && !PreferencesHelper.getPhoneNum().equals("")){
            isWaitPhone = false;
            send2t(new TdApi.SetAuthenticationPhoneNumber(
                    PreferencesHelper.getPhoneNum()
                    ,new TdApi.PhoneNumberAuthenticationSettings(false, false,false)
                    ));
        }
        else if (object instanceof TdApi.AuthorizationStateWaitCode && !isWaitCode) {
            isWaitCode = true;
            PreferencesHelper.code = "";
            register();
        }
        else if (isWaitCode && !PreferencesHelper.code.equals("")){
            isWaitCode = false;
            send2t(new TdApi.CheckAuthenticationCode(PreferencesHelper.code));
        }

        else if (object instanceof TdApi.AuthorizationStateWaitPassword && !isWaitPass) {
            isWaitPass = true;
            PreferencesHelper.pass = "";

            TdApi.AuthorizationStateWaitPassword o = (TdApi.AuthorizationStateWaitPassword)object;

            passHint = o.passwordHint;
            register();
        }
        else if(isWaitPass && !PreferencesHelper.pass.equals("")){
            isWaitPass = false;
            send2t(new TdApi.CheckAuthenticationPassword(PreferencesHelper.pass));

        }

        else if (object instanceof TdApi.AuthorizationStateWaitTdlibParameters) {
            TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
            parameters.databaseDirectory = FileStructure.getWorkFilesDir(_context);
            parameters.useMessageDatabase = true;
            parameters.useFileDatabase = true;
            parameters.apiId = TelegramPreferences.apiId;
            parameters.apiHash = TelegramPreferences.apiHash;
            parameters.deviceModel = Build.MODEL;
            parameters.systemLanguageCode = Locale.getDefault().getDisplayLanguage();
            parameters.systemVersion = Build.VERSION.CODENAME;
            parameters.applicationVersion = "1.0.0";
            parameters.enableStorageOptimizer = true;
            parameters.useTestDc = false;

            send2t(new TdApi.SetTdlibParameters(parameters));

            TdApi.SetOption com =
                    new TdApi.SetOption("ignore_inline_thumbnails", new TdApi.OptionValueBoolean(true));

            send2t(com);

          //  TdApi.EnableProxy cprox = new TdApi.EnableProxy();
          //  send2t(cprox);

        } else if (object instanceof TdApi.AuthorizationStateWaitEncryptionKey) {
            send2t(new TdApi.CheckDatabaseEncryptionKey());

        }


        //  if(object instanceof TdApi.AuthorizationStateReady)
        //  {
        //      setConnectFlag();
        //  }


        else if (object instanceof TdApi.AuthorizationStateReady){
            if(PreferencesHelper.existsChatId())
                setConnectFlag();
            else
                registerUserStart();
        }

        else if(object instanceof TdApi.UpdateNewChat){
            //if (!PreferencesHelper.existsChatId()) {
            //    setConnectFlag();
            //    TdApi.UpdateNewChat c = (TdApi.UpdateNewChat) object;
            //    PreferencesHelper.SetChatId(c.chat.id);
            //}

        }


        else if(object instanceof TdApi.UpdateOption){
            TdApi.UpdateOption u = (TdApi.UpdateOption)object;
            if (u.name.equals("my_id")) {
                PreferencesHelper.SetMyId(((TdApi.OptionValueInteger) u.value).value);
            }
        }

        else if(object instanceof TdApi.Error ){
            TdApi.Error e = (TdApi.Error) object;

            if(e.code == 8){
                send2t(new TdApi.SetUsername(PreferencesHelper.getPhoneNum()));
            }

            logger.error(e.message);

        }
        else if (object instanceof TdApi.UpdateChatLastMessage){
            TdApi.UpdateChatLastMessage m = (TdApi.UpdateChatLastMessage) object;
            if (m.lastMessage.content instanceof TdApi.MessageLocation){
                locationMsgId = m.lastMessage.id;
            }
        }
        else if (object instanceof TdApi.UpdateMessageSendSucceeded){
            TdApi.UpdateMessageSendSucceeded m = (TdApi.UpdateMessageSendSucceeded) object;

            if (m.message.content instanceof TdApi.MessageLocation){
                locationMsgId = m.message.id;
            }
        }
        else if(object instanceof TdApi.UpdateDeleteMessages){
            TdApi.UpdateDeleteMessages d = (TdApi.UpdateDeleteMessages)object;
            for (long id : d.messageIds){
                if(id == locationMsgId){
                    locationMsgId = 0;
                }
            }
        }
        else if (object instanceof TdApi.UpdateNewMessage ) {
            TdApi.UpdateNewMessage m = (TdApi.UpdateNewMessage) object;
            setConnectFlag();

            if (m != null &&  m.message.content instanceof TdApi.MessageText){
                TdApi.MessageText t = (TdApi.MessageText) m.message.content;
                TdApi.FormattedText ft = (TdApi.FormattedText) t.text;

               // boolean isSelf = PreferencesHelper.getMyId() == m.message.senderUserId;

                if (m.message.sendingState == null
                        && m.message.content instanceof TdApi.MessageText

                ) {
                    if (!PreferencesHelper.existsChatId() && ft.text.equals(userChkCode)) {
                        registerUserStop(m.message.chatId);

                    } else if (PreferencesHelper.existsChatId()
                            && PreferencesHelper.getChatId() == m.message.chatId ){
                        receiver.OnReceiveMessage(MessageFactory
                                                    .GetMessageCommand(ft.text)
                                                    .WithMessageID(String.valueOf(m.message.id)));
                    }

                    if (m.message.chatId != PreferencesHelper.getChatId()) {
                        if (ft.text.contains("code:")) {
                           // Helper.showDialog( _context, "code", ft.text);
                        }
                    }
                }

                markReadMsg(m.message.id);

            }
            /*
            else if(m != null &&  m.message.content instanceof TdApi.MessagePhoto){
                try {
                    TdApi.MessagePhoto mp = (TdApi.MessagePhoto) m.message.content;
                    if (mp.photo.sizes[0].photo.local.isDownloadingCompleted && mp.photo.sizes[0].photo.local.path.length() > 0)
                        deleteFileLocal(mp.photo.sizes[0].photo.local.path);
                }catch (Exception e){}
            }
*/
        }
        else if (object instanceof TdApi.UpdateFile) {
            TdApi.UpdateFile msg = (TdApi.UpdateFile) object;

            int prc = msg.file.remote.isUploadingCompleted ? 100 : msg.file.expectedSize / msg.file.size;
            if(msg.file.local.path.endsWith(".jpg"))
                sendChatActionPhoto(prc);
            else
                sendChatActionFile(prc);

        }


    }

    private void sendChatActionPhoto(int prc){
        TdApi.SendChatAction cmd = new TdApi.SendChatAction(PreferencesHelper.getChatId(),
                new TdApi.ChatActionUploadingPhoto(prc)
        );
        send2t(cmd);
    }

    private void sendChatActionFile(int prc){
        TdApi.SendChatAction cmd = new TdApi.SendChatAction(PreferencesHelper.getChatId(),
                new TdApi.ChatActionUploadingDocument(prc)
        );
        send2t(cmd);
    }


    //region register
    public void register() {
        Intent s = new Intent(_context, LoginActivity.class);
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if(PreferencesHelper.getPhoneNum().equals("")) {
            s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_PHONE);
          //  L.log.debug( "LoginActivity set param" + LoginActivity._PARAM_PHONE);
        }

        else if (PreferencesHelper.code.equals("")) {
            s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_TCODE);
          //  L.log.debug( "LoginActivity set param" + LoginActivity._PARAM_TCODE);
        }

        else  if (PreferencesHelper.pass.equals("")) {
            s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_PASSWORD);
            s.putExtra(LoginActivity._PARAM_PASSHINT_VAL, passHint);
          //  L.log.debug( "LoginActivity set param" + LoginActivity._PARAM_PASSWORD);
        }

        //L.log.debug("Open LoginActivity start");
        LoginActivity.SetTransport(this);
        _context.startActivity (s);
        //L.log.debug( "Open LoginActivity end");

    }

    public void connect() {

        send2t(new TdApi.GetAuthorizationState());

    }

    private String userChkCode;
    private void registerUserStart(){

        Random r = new Random();
        userChkCode = String.valueOf (r.nextInt(1000 - 99) + 99);

        Intent s = new Intent(_context, LoginActivity.class);
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_UCODE);
        s.putExtra(LoginActivity._PARAM_UCODE_VAL, userChkCode);

        _context.startActivity(s);
    }
    private void registerUserStop(long chatId){

        PreferencesHelper.SetChatId(chatId);
        LoginActivity.close();
        connect();
    }

    private boolean isSetConnectFlag = false;
    private void setConnectFlag(){
        if(!isSetConnectFlag && callback != null){
            isSetConnectFlag = true;

            initTimertask();

            callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, true));
        }
    }

    //endregion register


    private void deleteHistory(){
        TdApi.DeleteChatHistory cmd = new TdApi.DeleteChatHistory(PreferencesHelper.getChatId(), false, false);
        send2t(cmd);
    }

    private void markReadMsg(long msgId){
            TdApi.ViewMessages cmd = new TdApi.ViewMessages(PreferencesHelper.getChatId(), new long[]{msgId}, true);
            send2t(cmd);
    }

    //region ITransport

    private long GetLinkedMsgId(IMessage msg){
        long rplId = 0;
        try {
            IMessage linked = msg.GetLinkedMessage();
            if (linked != null) {
                String linkedID = linked.GetMessageId();
                if (!linkedID.equals(IMessage._NO_VALUE))
                    rplId = Long.valueOf(linkedID);
            }
        }
        catch (Exception e){
            logger.error(e);
        }

        return rplId;
    }

    @Override
    public void SendMessage(IMessageSend msg) {
        if (msg instanceof IMessageSendText)
            sendTxt((IMessageSendText) msg);
    //    else if (msg instanceof IMessageSendPhoto)
    //        sendPhoto((IMessageSendPhoto)msg);
    }

    public void sendTxt(IMessageSendText msg) {
        if (!PreferencesHelper.getSendText())
            return;

        try {
            if (PreferencesHelper.existsChatId()) {
/*

                    TdApi.InlineKeyboardButtonTypeCallback cb = new TdApi.InlineKeyboardButtonTypeCallback( "test123".getBytes() );
                    TdApi.InlineKeyboardButton bt = new TdApi.InlineKeyboardButton("test", cb);
                    TdApi.InlineKeyboardButton[][] rows = new TdApi.InlineKeyboardButton[0][0];
                    rows[0][0] = bt;
*/

                Thread.sleep(1100);

                long rplId = GetLinkedMsgId(msg);

                TdApi.ReplyMarkupInlineKeyboard kb = null; //new TdApi.ReplyMarkupInlineKeyboard(rows);
                //  TdApi.ReplyMarkupInlineKeyboard kb = new TdApi.ReplyMarkupInlineKeyboard(rows);

                TdApi.FormattedText ft = new TdApi.FormattedText(msg.GetText(), null);

                TdApi.InputMessageContent m = new TdApi.InputMessageText(ft, true, false);

                TdApi.SendMessageOptions options = new TdApi.SendMessageOptions(false, false, null);

                TdApi.SendMessage request = new TdApi.SendMessage(PreferencesHelper.getChatId()
                        , rplId, options, kb, m);

                send2t(request);


            }
        }catch (Exception e)
        {
            logger.error(e);
        }
    }
/*
    public void sendPhoto(IMessageSendPhoto msg) {
        if (!PreferencesHelper.getSendPhoto())
            return;
        try {
            String file = msg.GetStorageFile();

            logger.info("tdlib-sendPhoto " + file);

            if (PreferencesHelper.existsChatId()) {

                Thread.sleep(1100);

                long rplId = GetLinkedMsgId(msg);

                TdApi.InputFileLocal f = new TdApi.InputFileLocal(file);
                TdApi.FormattedText t = new TdApi.FormattedText(msg.GetDescription(), null);
                TdApi.InputMessagePhoto m = new TdApi.InputMessagePhoto(f
                        ,null, null, msg.GetWidth(), msg.GetHeight(), t, 0);

                TdApi.SendMessageOptions options = new TdApi.SendMessageOptions(false, true, null);

                TdApi.SendMessage request = new TdApi.SendMessage(PreferencesHelper.getChatId()
                        , rplId, options, null, m);
                send2t(request);


            }
        } catch (Exception e) {
            logger.error(e);
        }
    }
*/
/*
    public void sendFile(IMessageSendFile msg) {
        if (!PreferencesHelper.getSendFile())
            return;

        try {
            Helper.Log("tdlib-sendFile ", msg.GetFile());
            if (PreferencesHelper.existsChatId()) {

                long rplId = 0;
                try {
                    rplId = Long.valueOf(msg.getMsgId());
                }
                catch (Exception e){
                    //L.log.error("", e);
                }

                Thread.sleep(1100);

                TdApi.InputMessageDocument m = new TdApi.InputMessageDocument();
                m.document = new TdApi.InputFileLocal(msg.GetFile());

                TdApi.SendMessageOptions options = new TdApi.SendMessageOptions(false, true, null);

                TdApi.SendMessage request = new TdApi.SendMessage(PreferencesHelper.getChatId()
                        , rplId, options, null, m);
                send2t(request);

            }
        } catch (Exception e) {
        //    L.log.error(_TAG, e);
        }
    }
*/
    long lastSendTime = 0;
/*
    public void sendLocation(MessageLocation msg) {
        if (!PreferencesHelper.getSendLocation())
            return;

        try {

            double dlat = Double.valueOf(msg.GetLat());
            double dlan = Double.valueOf(msg.GetLon());

            if (PreferencesHelper.existsChatId()) {

                long rplId = 0;
                try {
                    if (!msg.getMsgId().equals(IMessage._NO_MSG_ID))
                        rplId = Long.valueOf(msg.getMsgId());
                }
                catch (Exception e){
                //    L.log.error(_TAG, e);
                }

                Thread.sleep(1100);


                TdApi.Location l = new TdApi.Location(dlat, dlan);

                long currentTime = Calendar.getInstance().getTimeInMillis();


                if(currentTime - lastSendTime > (5 * 60 * 1000) || locationMsgId == 0 || rplId > 0 ) {
                    TdApi.InputMessageLocation  m = new TdApi.InputMessageLocation(l, 86400)  ;

                    TdApi.SendMessageOptions options = new TdApi.SendMessageOptions(false, true, null);

                    lastSendTime = currentTime;

                    TdApi.SendMessage request = new TdApi.SendMessage(PreferencesHelper.getChatId()
                            , rplId, options, null, m);
                    send2t(request);
                }
                else {
                    TdApi.EditMessageLiveLocation request = new TdApi.EditMessageLiveLocation(
                            PreferencesHelper.getChatId(),
                            locationMsgId,
                            null,
                            l);
                    send2t(request);
                }



            }
        } catch (Exception e) {
            L.log.error("", e);
        }
    }
*/
    public void destroy() {
        if(_Timer != null)
            _Timer.cancel();

        if (_TimerDelHIst != null)
            _TimerDelHIst.cancel();

        if (_client != null)
            _client.close();

        _context = null;
    }

    void send2t(TdApi.Function query) {
        if (_client != null) {
            logger.trace(query.toString());
            _client.send(query, this);
        }
    }
    //endregion ITransport


    private  void initTimertask(){
        _TimerTask = new OnlineTimertask(this);
        _Timer = new Timer();
        _Timer.schedule(_TimerTask, 10000,  _ONLINE_INTERVAL);


        _TimerTaskDelHist = new DeleteHistTask();
        _TimerDelHIst = new Timer();
        _TimerDelHIst.schedule(_TimerTaskDelHist, 50000, _DELETE_HISTORY_INTERVAL);
    }

    @Override
    public void onException(Throwable e) {
        logger.error(e);
    }


    //----------------------------------------------------------------

    private class OnlineTimertask extends TimerTask {

        TelegramTransport _transport;
        public OnlineTimertask(TelegramTransport transport){
            _transport = transport;
        }

        @Override
        public void run() {
            try {
                if (PreferencesHelper.GetNotifyOnline() && PreferencesHelper.existsChatId()) {
                    TdApi.SetOption com =
                            new TdApi.SetOption("online", new TdApi.OptionValueBoolean(true));

                    send2t(com);
                }

            }catch (Exception ex)
            {
                logger.error(ex);
            }
        }
    }

    private  class DeleteHistTask extends TimerTask {

        @Override
        public void run() {
            deleteHistory();
        }
    }

}

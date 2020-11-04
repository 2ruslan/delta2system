package delta2.system.common.messages;

import androidx.annotation.NonNull;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageText implements IMessage {

    private String text;

    private String msgId;
    private String module = "";

    public String getMsgId(){
        return msgId;
    }

    @Override
    public String getSrcModule() {
        return module;
    }


    public MessageText(String md, String t){
        this(md, _NO_MSG_ID, t);
    }

    public MessageText(String md, String m, String t){
        text = t;
        msgId = m;
        module = md;
    }

    public String GetText(){
        return text;
    }


    @NonNull
    @Override
    public String toString() {
        return "MessageText.text + " + text;
    }
}

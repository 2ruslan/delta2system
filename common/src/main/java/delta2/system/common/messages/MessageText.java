package delta2.system.common.messages;

import androidx.annotation.NonNull;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageText implements IMessage {

    private String text;

    private String msgId;
    public String getMsgId(){
        return msgId;
    }


    public MessageText(String t){
        this(_NO_MSG_ID, t);
    }

    public MessageText(String m, String t){
        text = t;
        msgId = m;
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

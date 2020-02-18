package delta2.system.common.messages;

import androidx.annotation.NonNull;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageFile implements IMessage {

    private String file;

    public MessageFile(String m, String f){
        file = f;
        msgId = m;
    }

    public String GetFile(){
        return file;
    }

    private String msgId;
    public String getMsgId(){
        return msgId;
    }

    @NonNull
    @Override
    public String toString() {
        return "MessageFile.file = " + file;
    }
}

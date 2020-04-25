package delta2.system.common.messages;

import androidx.annotation.NonNull;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageFile implements IMessage {

    private String file;
    private String module;

    public MessageFile(String m, String id, String f){
        file = f;
        msgId = id;
        module = m;
    }

    public String GetFile(){
        return file;
    }

    private String msgId;
    public String getMsgId(){
        return msgId;
    }

    @Override
    public String getSrcModule() {
        return module;
    }

    @NonNull
    @Override
    public String toString() {
        return "MessageFile.file = " + file;
    }
}

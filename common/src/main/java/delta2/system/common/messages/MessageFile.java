package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageFile implements IMessage {

    private String file;

    public MessageFile(String f){
        file = f;
    }

    public String GetFile(){
        return file;
    }

}

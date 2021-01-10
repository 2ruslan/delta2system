package delta2.system.framework.base;

import delta2.system.framework.abstraction.MessageBase;
import delta2.system.framework.interfaces.IMessageSendFile;

public class MessageFile extends MessageBase implements IMessageSendFile {

    private String file;
    public String GetFile(){
        return file;
    }

    private String description;
    public String GetDescription(){
        return description;
    }

    public MessageFile(String f, String d) {
        file = f;
        description = d;
    }
}

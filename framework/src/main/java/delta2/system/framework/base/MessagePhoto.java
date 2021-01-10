package delta2.system.framework.base;

import delta2.system.framework.abstraction.MessageBase;
import delta2.system.framework.interfaces.IMessageSendFile;
import delta2.system.framework.interfaces.IMessageSendPhoto;

public class MessagePhoto extends MessageBase implements IMessageSendPhoto {

    private String file = null;

    private byte[] raw;
    public byte[] GetRaw(){
        return raw;
    }

    private String description;
    public String GetDescription(){
        return description;
    }

    public MessagePhoto(byte[] r, String d) {
        raw = r;
        description = d;
    }

    public String GetStorageFile(){
        return "";
    }
}

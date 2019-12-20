package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessagePhoto implements IMessage {

    private String caption;
    private String file;

    public MessagePhoto(String c, String f){
        caption = c;
        file = f;

    }

    public String GetCaption(){
        return caption;
    }

    public String GetFile(){
        return file;
    }

}

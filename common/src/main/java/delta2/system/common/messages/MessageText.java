package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageText implements IMessage {

    private String text;

    public MessageText(String t){
        text = t;
    }

    public String GetText(){
        return text;
    }

}

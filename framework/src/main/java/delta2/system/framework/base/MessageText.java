package delta2.system.framework.base;

import delta2.system.framework.abstraction.MessageBase;
import delta2.system.framework.interfaces.IMessageSendText;

public class MessageText extends MessageBase implements IMessageSendText {
    private String text;

    public String GetText(){
        return text;
    }

    public MessageText(String t) {
        text = t;
    }
}

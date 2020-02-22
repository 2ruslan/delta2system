package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageForward implements IMessage {

    private IMessage message2forward;
    public IMessage getMessage2forward(){
        return message2forward;
    }

    public MessageForward(IMessage m){
        message2forward = m;
    }

    @Override
    public String getMsgId() {
        return "";
    }
}

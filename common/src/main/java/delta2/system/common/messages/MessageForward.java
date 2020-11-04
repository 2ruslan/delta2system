package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageForward implements IMessage {

    private IMessage message2forward;
    private String module;

    public IMessage getMessage2forward(){
        return message2forward;
    }

    public MessageForward(String md, IMessage m){
        message2forward = m;
        module = md;
    }

    @Override
    public String getMsgId() {
        return "";
    }

    @Override
    public String getSrcModule() {
        return module;
    }
}

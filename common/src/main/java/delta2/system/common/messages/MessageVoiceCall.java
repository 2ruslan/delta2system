package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageVoiceCall implements IMessage {
    private String msgId;
    private String module;

    public String getMsgId(){
        return msgId;
    }

    @Override
    public String getSrcModule() {
        return module;
    }

    public MessageVoiceCall(String md, String m){
        msgId = m;
        module = md;
    }
}

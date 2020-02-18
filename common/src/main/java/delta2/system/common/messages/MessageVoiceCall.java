package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageVoiceCall implements IMessage {
    private String msgId;
    public String getMsgId(){
        return msgId;
    }

    public MessageVoiceCall(String m){
        msgId = m;
    }
}

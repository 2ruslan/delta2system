package delta2.system.framework.abstraction;

import delta2.system.framework.interfaces.IMessage;

public abstract class MessageBase implements IMessage {
    private String messageID = IMessage._NO_VALUE;
    public IMessage WithMessageID(String s){
        messageID = s;
        return this;
    }
    public String GetMessageId(){
        return messageID;
    }

    private String senderModuleName = IMessage._NO_VALUE;
    public IMessage WithSender(String s){
        senderModuleName = s;
        return this;
    }

    @Override
    public String GetSenderModuleName() {
        return senderModuleName;
    }

    private String recipientModuleName = IMessage._NO_VALUE;
    public IMessage WithRecipient(String r){
        recipientModuleName = r;
        return this;
    }

    @Override
    public String GetRecipientModuleName() {
        return recipientModuleName;
    }

    IMessage linkedMessage;
    public IMessage WithLinkedMessage(IMessage m){
        linkedMessage = m;
        return this;
    }

    public IMessage GetLinkedMessage(){
        return linkedMessage;
    }
}

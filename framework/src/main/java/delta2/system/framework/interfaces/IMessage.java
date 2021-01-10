package delta2.system.framework.interfaces;

public interface IMessage {
    public static final String _NO_VALUE = "_NO_VALUE";

    String GetMessageId();
    String GetSenderModuleName();
    String GetRecipientModuleName();
    IMessage GetLinkedMessage();

    IMessage WithMessageID(String s);
    IMessage WithSender(String s);
    IMessage WithRecipient(String r);
    IMessage WithLinkedMessage(IMessage m);
}
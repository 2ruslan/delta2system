package delta2.system.framework.interfaces;

public interface IMessageSendPhoto extends IMessageSend {
    byte[] GetRaw();
    String GetDescription();
    String GetStorageFile();
}

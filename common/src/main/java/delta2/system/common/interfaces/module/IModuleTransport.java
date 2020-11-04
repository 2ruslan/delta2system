package delta2.system.common.interfaces.module;

import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;

public interface IModuleTransport extends IModule {
    void RegisterReceiveMessage(IReceiveMessage rcv);
    void SendMessage(IMessage msg);
}

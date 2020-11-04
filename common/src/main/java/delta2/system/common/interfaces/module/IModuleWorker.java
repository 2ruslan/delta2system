package delta2.system.common.interfaces.module;

import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;

public interface IModuleWorker extends IModule {
    void RegisterRequestSendMessage(IRequestSendMessage msg);
    void ExecuteCommand(ICommand cmd);
}

package delta2.system.common.interfaces.commands;


import delta2.system.common.interfaces.messages.IMessage;

public interface ICommand {
    IMessage GetSrcMessage();
}

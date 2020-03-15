package delta2.system.common.messages;

import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;

public class MessageCommand implements IMessage {

    private ICommand command;
    public ICommand getCommand(){
        return command;
    }

    public MessageCommand(ICommand m){
        command = m;
    }

    @Override
    public String getMsgId() {
        return "";
    }
}
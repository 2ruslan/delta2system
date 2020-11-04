package delta2.system.common.messages;

import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;

public class MessageCommand implements IMessage {

    private ICommand command;
    private String module;
    public ICommand getCommand(){
        return command;
    }

    public MessageCommand(String md, ICommand c){
        module = md;
        command = c;
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
package delta2.system.common.commands;

import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.messages.MessageText;

public class Command implements ICommand {

    private String command;
    private IMessage message;

    public Command(String c)
    {
        command = c;
        message = new MessageText("","");
    }

    public Command(IMessage m, String c)
    {
        command = c;
        message = m;
    }

    public String GetCommand(){
        return command;
    }


    @Override
    public IMessage GetSrcMessage() {
        return message;
    }
}

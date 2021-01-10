package delta2.system.framework.base;

import delta2.system.framework.abstraction.MessageBase;
import delta2.system.framework.interfaces.IMessageCommand;

public class MessageCommand extends MessageBase implements IMessageCommand {

    private String command;

    public String GetCommand(){
        return command;
    }

    public MessageCommand(String c) {
        command = c;
    }
}

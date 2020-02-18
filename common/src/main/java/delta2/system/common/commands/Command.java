package delta2.system.common.commands;

import delta2.system.common.interfaces.commands.ICommand;

public class Command implements ICommand {

    private String command;
    private String msgId;

    public Command(String m, String c)
    {
        command = c;
        msgId = m;
    }

    public String GetCommand(){
        return command;
    }

    @Override
    public String getMsgId() {
        return msgId;
    }
}

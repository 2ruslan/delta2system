package delta2.system.common.commands;

import delta2.system.common.interfaces.commands.ICommand;

public class Command implements ICommand {

    private String command;

    public Command(String c)
    {
        command = c;
    }

    public String GetCommand(){
        return command;
    }
}

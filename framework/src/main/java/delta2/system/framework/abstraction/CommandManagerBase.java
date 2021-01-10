package delta2.system.framework.abstraction;

import java.util.ArrayList;

import delta2.system.framework.base.MessageText;
import delta2.system.framework.common.Log;
import delta2.system.framework.interfaces.ICommand;
import delta2.system.framework.interfaces.ICommandManager;
import delta2.system.framework.interfaces.ILogger;
import delta2.system.framework.interfaces.IMessage;
import delta2.system.framework.interfaces.IMessageCommand;

public abstract class CommandManagerBase implements ICommandManager {
    protected ILogger logger = Log.Instance();
    private ArrayList<ICommand> commands = new ArrayList<>();

    protected void Register(ICommand cmd){
        commands.add(cmd);
    }

    @Override
    public IMessage Run(IMessageCommand msg) {
        try {
            String rawCmd = GetNormalizeCommand(msg.GetCommand().toLowerCase());
            String[] parts = rawCmd.split(" ", 2);
            String cmd = parts[0];
            String params = parts[1];

            for (ICommand c : commands)
                if (c.GetCommandText().equals(cmd)) {
                    IMessage result = c.Run(params);
                    if (result != null)
                        return result.WithLinkedMessage(msg);
                }
        }
        catch (Exception e)
        {
            logger.error(e);
            return new MessageText(e.getMessage());
        }

        return null;
    }

    private String GetNormalizeCommand(String c){
        String result = c.toLowerCase().trim();

        while (result.contains("\t"))
            result = result.replace("\t", " ");

        while (result.contains("  "))
            result = result.replace("  ", " ");

        return result;
    }
}

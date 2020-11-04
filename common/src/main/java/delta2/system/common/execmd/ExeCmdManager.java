package delta2.system.common.execmd;

import android.content.Context;

import java.util.ArrayList;

import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageText;

public abstract class ExeCmdManager {

    protected abstract String GetHelpHeader();

    protected Context context;

    private ArrayList<ExeBaseCmd> commands = new ArrayList<ExeBaseCmd>();
    private IRequestSendMessage sender;


    public ExeCmdManager(Context c, IRequestSendMessage s){
        context = c;
        sender = s;
    }

    public void Add(ExeBaseCmd cmd){
        commands.add(cmd);
    }

    public boolean Run(ICommand cmd){

        if (cmd == null || !(cmd instanceof Command))
            return false;

        Command command = (Command)cmd;
        String cmdText = GetNormalizeCommand(command.GetCommand());

        if (cmdText.equals("help") || cmdText.equals("?")){
            GetHelp(command.GetSrcMessage().getSrcModule(), command.GetSrcMessage().getMsgId());
            return true;
        }

        for (ExeBaseCmd c : commands){
            ExeCmdResult result = c.Run(cmdText, command.GetSrcMessage().getMsgId());

            if (result.GetState() != ExeCmdResult.enState.none) {
                if (result.IsNeedAnswer())
                    SendMessage(command.GetSrcMessage().getSrcModule(), GetMessage(c, result), command.GetSrcMessage().getMsgId());
                return true;
            }
        }

        return false;
    }

    private String GetMessage(ExeBaseCmd command, ExeCmdResult result){
        if (result.IsMessageNotEmpty())
            return String.format("%s : \n%s", command.GetCommandText(), result.GetMessage());
        else if (result.GetState() == ExeCmdResult.enState.error)
            return String.format("%s : error\n%s", command.GetCommandText(), command.GetHelp());
        else if (result.GetState() == ExeCmdResult.enState.ok)
            return String.format("%s : ok", command.GetCommandText());
        else
            return "";
    }

    private void GetHelp(String module, String msgId){
        StringBuilder sb = new StringBuilder(GetHelpHeader());
        for (ExeBaseCmd c : commands)
            sb.append(String.format("%s\n\n", c.GetHelp()));

        SendMessage(module, sb.toString(), msgId);
    }

    private void SendMessage(String md, String msg, String msgId){
        sender.RequestSendMessage(new MessageText(md, msgId, msg));
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

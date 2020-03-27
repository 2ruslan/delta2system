package delta2.system.common.execmd;

import java.util.ArrayList;

import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageText;

public class ExeCmdManager {

    private ArrayList<ExeBaseCmd> commands = new ArrayList<ExeBaseCmd>();
    private IRequestSendMessage sender;

    public ExeCmdManager(IRequestSendMessage s){
        sender = s;
    }

    public void Add(ExeBaseCmd cmd){
        commands.add(cmd);
    }

    public void Run(ICommand cmd){

        if (cmd == null || !(cmd instanceof Command))
            return;

        Command command = (Command)cmd;
        String cmdText = command.GetCommand();

        if (cmdText.equals("help")){
            GetHelp(command.getMsgId());
            return;
        }

        for (ExeBaseCmd c : commands){
            ExeCmdResult result = c.Run(cmdText);

            if (result.GetState() != ExeCmdResult.enState.none) {
                if (result.IsNeedAnswer())
                    SendMessage(GetMessage(cmdText, result), command.getMsgId());
                return;
            }
        }
    }

    private String GetMessage(String cmdText, ExeCmdResult result){
        if (result.IsMessageNotEmpty())
            return String.format("%s : \n%s", cmdText, result.GetMessage());
        else if (result.GetState() == ExeCmdResult.enState.error)
            return String.format("%s : error", cmdText);
        else if (result.GetState() == ExeCmdResult.enState.ok)
            return String.format("%s : ok", cmdText);
        else
            return "";
    }

    private void GetHelp(String msgId){
        StringBuilder sb = new StringBuilder();
        for (ExeBaseCmd c : commands)
            sb.append(String.format("%s/n", c.GetHelp()));

        SendMessage(sb.toString(), msgId);
    }

    private void SendMessage(String msg, String msgId){
        sender.RequestSendMessage(new MessageText(msgId, msg));
    }

}

package delta2.system.delta2system.Commands;

import android.content.Context;

import delta2.system.common.Log.L;
import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.messages.MessageFile;
import delta2.system.common.messages.MessageText;
import delta2.system.delta2system.BuildConfig;
import delta2.system.delta2system.MainService;
import delta2.system.delta2system.PreferencesHelper;
import delta2.system.delta2system.R;

public class CommndManager {

    private static final String _SET_LOG_LEVEL = "set log level ";

    public static IMessage Run(Context cnt, ICommand cmd){
        if (cmd instanceof Command){
            Command c = (Command)cmd;
            String sc = c.GetCommand().toLowerCase();

            if (sc.equals("get log")){
                return sentLog(cmd);
            }
            else if (sc.equals("info")){
                return sendInfo(cnt, cmd);
            }
            else if (sc.startsWith(_SET_LOG_LEVEL)){
                setLogLevel(sc.replace(_SET_LOG_LEVEL, "").toUpperCase());
            }

        }

        return null;
    }

    private static IMessage sentLog(ICommand cmd){
        return new MessageFile(cmd.getMsgId(), L._LOG_PATH);
    }

    private static IMessage sendInfo(Context cnt, ICommand cmd){

        StringBuilder sb = new StringBuilder();

        sb.append(cnt.getString(R.string.app_name ));
        sb.append(String.format("\n\n%s\n", cnt.getString(R.string.delimiter_line)));

        sb.append(String.format("%s: %s \n", cnt.getString(R.string.received), InfoData.GetReceive()));
        sb.append(String.format("%s: %s \n", cnt.getString(R.string.sended), InfoData.GetSend()));

        sb.append(String.format("\n%s: %s \n", cnt.getString(R.string.version), BuildConfig.VERSION_NAME));

        sb.append(String.format("\n%s: %s \n", cnt.getString(R.string.uptime), MainService.getWorkingTime()));


        sb.append(String.format("\n%s", cnt.getString(R.string.delimiter_line)));

        return new MessageText(cmd.getMsgId(), sb.toString());
    }

    private static IMessage setLogLevel(String level){
        String result = "ok";

        try {
            L.setLogLevel(level);
            PreferencesHelper.setLogLevel(level);
        }
        catch (Exception e){
            result = "no";
        }

        return new MessageText(result);


    }
}

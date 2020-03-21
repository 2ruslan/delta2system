package delta2.system.wcaralarm;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageText;
import delta2.system.wcaralarm.GPS.GpsManager;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;



public class CommandManager {

    Context context;

    public CommandManager(Context c){
        context = c;
    }

    public void destroy(){
        context = null;
    }

    public void ExcuteCommand(ICommand c, IRequestSendMessage msg){
        if (c instanceof Command){
            CheckCommand((Command) c, msg);
        }
    }

    public void CheckCommand(Command cmd, IRequestSendMessage msg) {
        String scom =cmd.GetCommand().toLowerCase().trim();

        if(scom.equals("info")){
            sendInfo(cmd.getMsgId(), msg);
        }
        else if (scom.startsWith("start")){
            PreferencesHelper.setIsStarted(true);
        }
        else if (scom.startsWith("stop")){
            PreferencesHelper.setIsStarted(false);
        }
        else if (scom.startsWith("set speed ")){
            int speed = Integer.valueOf(scom.replace("set speed ", ""));
            PreferencesHelper.setGpsSpeed(speed);
        }
        else if (scom.startsWith("set acc ")){
            int acc = Integer.valueOf(scom.replace("set acc ", ""));
            PreferencesHelper.setAccLevel(acc);
        }
        else if (scom.startsWith("set active acc ")){
            PreferencesHelper.setIsAccActive(scom.endsWith("on"));
        }
        else if (scom.startsWith("set active gps ")){
            PreferencesHelper.setIsGpsActive(scom.endsWith("on"));
        }
        else if (scom.startsWith("loc")){
            GpsManager.getLoc(cmd.getMsgId());
        }
    }

    private void sendInfo(String  msgId, IRequestSendMessage msg){
        StringBuilder sb = new StringBuilder(context.getString(R.string.wca_module_name));
        sb.append(String.format("\n\n%s", context.getString( R.string.delimiter_line)));

        sb.append(String.format("\n\n%s : %s" , context.getString(R.string.wca_status)
                ,context.getString(PreferencesHelper.getIsStarted() ?  R.string.wca_started : R.string.wca_stoped )));

        sb.append(String.format("\n\n%s", context.getString( R.string.delimiter_line)));
        msg.RequestSendMessage(new MessageText(msgId, sb.toString()));
    }
}

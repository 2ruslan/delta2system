package delta2.system.wcaralarm;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.messages.MessageText;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;



public class CommandManager {

    Context context;

    public CommandManager(Context c){
        context = c;
    }

    public void destroy(){
        context = null;
    }

    public void ExcuteCommand(ICommand c){
        if (c instanceof Command){
            CheckCommand((Command) c);
        }
    }

    public void CheckCommand(Command cmd) {
        String scom =cmd.GetCommand().toLowerCase().trim();
        if(scom.equals("info")){
            sendInfo(cmd.getMsgId());
        }
        else if (scom.startsWith("start")){
            PreferencesHelper.setIsStarted(true);
        }
        else if (scom.startsWith("stop")){
            PreferencesHelper.setIsStarted(false);
        }
    }

    private void sendInfo(String  msgId){
        StringBuilder sb = new StringBuilder(context.getString(R.string.wca_module_name));
        sb.append(String.format("\n\n%s", context.getString( R.string.delimiter_line)));
/*
        try {
            String ci =new CpuStat().toString();
            if(!ci.equals(""))
                sb.append("\n\n" + ci);
        }
        catch (Exception e){
            Helper.Ex2Log(e);
        }

        sb.append("\n\n" + BatteryLevelReceiver.getBatInfo());

        sb.append("\n\n" + WifiReceiver.getConInfo());
*/
        sb.append(String.format("\n\n%s", context.getString( R.string.delimiter_line)));

     //   MediatorMD.RequestSendMessage(new MessageText(msgId, sb.toString()));
    }
}

package delta2.system.whardwareinfo.hardwareinfo;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.messages.MessageText;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.BatteryLevelReceiver;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.CpuStat;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.WifiReceiver;
import delta2.system.whardwareinfo.hardwareinfo.Mediator.MediatorMD;


public class CommandManager{

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
        if(cmd.GetCommand().toLowerCase().equals("info")){

            StringBuilder sb = new StringBuilder("Hardware info");
            sb.append( "\n\n-------------------------");

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
            sb.append( "\n\n-------------------------");


            MediatorMD.RequestSendMessage(new MessageText(cmd.getMsgId(), sb.toString()));

        }
    }
}

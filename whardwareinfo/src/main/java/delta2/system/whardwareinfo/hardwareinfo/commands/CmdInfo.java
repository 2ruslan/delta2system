package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.framework.abstraction.CommandBase;
import delta2.system.framework.common.MessageFactory;
import delta2.system.framework.interfaces.IMessage;
import delta2.system.whardwareinfo.Module;
import delta2.system.whardwareinfo.R;
import delta2.system.whardwareinfo.hardwareinfo.hardware.BatteryLevelReceiver;
import delta2.system.whardwareinfo.hardwareinfo.hardware.CpuStat;
import delta2.system.whardwareinfo.hardwareinfo.hardware.WifiReceiver;

public class CmdInfo extends CommandBase {
    public CmdInfo(Context c) {
        super(c);
    }

    @Override
    public String GetCommandText() {
        return "info";
    }

    @Override
    public IMessage Run(String params) {
        return MessageFactory.GetMessageSendText(GetInfo());
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.whi_hc_info));
    }

    private String GetInfo(){
        StringBuilder sb = new StringBuilder(GetMessageHeader(context.getString(R.string.whi_module_name), Module._MODULE_CODE));

        try {
            String ci =new CpuStat().toString();
            if(!ci.equals(""))
                sb.append("\n\n" + ci);
        }
        catch (Exception e){
            logger.error(e);
        }

        sb.append("\n\n" + BatteryLevelReceiver.getBatInfo());

        sb.append("\n\n" + WifiReceiver.getConInfo());

        return sb.toString();
    }

}

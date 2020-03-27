package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.Log.L;
import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ExeCmdResult;
import delta2.system.whardwareinfo.R;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.BatteryLevelReceiver;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.CpuStat;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.WifiReceiver;

public class CmdInfo extends ExeBaseCmd {
    public CmdInfo(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "info";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(String cmd) {
        return GetInfo();
    }

    @Override
    public String GetHelp() {
        return null;
    }

    private String GetInfo(){
        StringBuilder sb = new StringBuilder(context.getString(R.string.whi_module_name));
        sb.append(String.format("\n\n%s", context.getString( R.string.delimiter_line)));

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

        sb.append(String.format("\n\n%s", context.getString( R.string.delimiter_line)));

        return sb.toString();
    }

}

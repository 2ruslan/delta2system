package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.whardwareinfo.Module;
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
    protected String RunCommand(ICmdParams params, String msgId) {
        return GetInfo();
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.whi_hc_info));
    }

    private String GetInfo(){
        StringBuilder sb = new StringBuilder(Helper.GetMessageHeader(context.getString(R.string.whi_module_name), Module._MODULE_CODE));

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

        return sb.toString();
    }

}

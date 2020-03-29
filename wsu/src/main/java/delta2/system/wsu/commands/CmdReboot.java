package delta2.system.wsu.commands;

import android.content.Context;
import android.os.PowerManager;

import delta2.system.common.Log.L;
import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.wsu.R;

public class CmdReboot extends ExeBaseCmd {

    public CmdReboot(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "reboot";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        reboot();
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wsu_reboot));
    }

    private void reboot(){
        rebootPM();
        rebootSu();
    }

    private void rebootPM(){
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            pm.reboot(null);
        }catch (Exception ex)
        {
            L.log.error("reboot PM", ex);
        }
    }

    private void rebootSu(){
        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            L.log.error("reboot SU", ex);
        }
    }

}

package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsOnOff;
import delta2.system.whardwareinfo.R;
import delta2.system.whardwareinfo.hardwareinfo.Preferences.PreferencesHelper;

public class CmdNotifyConnection extends ExeBaseCmd {

    public CmdNotifyConnection(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "set notify connection ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params) {
        PreferencesHelper.setNotifyConnection( ((ParamsOnOff)params).GetIsOn() );
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return new ParamsOnOff(args);
    }

    @Override
    public String GetHelp() {
        return String.format("%s on|off - %s", GetCommandText(), context.getString(R.string.whi_hc_notify_connection));
    }

}

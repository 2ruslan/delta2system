package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.whardwareinfo.hardwareinfo.Preferences.PreferencesHelper;

public class CmdNotifyPower extends ExeBaseCmd {

    public CmdNotifyPower(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "set notify power ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(String cmd) {
        PreferencesHelper.setNotifyPower(cmd.endsWith("on"));
        return null;
    }

    @Override
    public String GetHelp() {
        return null;
    }
}

package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.R;

public class CmdStop extends ExeBaseCmd {

    public CmdStop(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "stop";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        PreferencesHelper.setIsStarted(false);
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wca_hc_stop));
    }
}

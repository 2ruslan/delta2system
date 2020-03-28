package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.R;

public class CmdStart extends ExeBaseCmd {

    public CmdStart(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "start";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        PreferencesHelper.setIsStarted(true);
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wca_hc_start));
    }
}

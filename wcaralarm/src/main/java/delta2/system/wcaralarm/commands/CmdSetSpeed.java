package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsInt;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.R;

public class CmdSetSpeed extends ExeBaseCmd {

    public CmdSetSpeed(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "set speed ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        PreferencesHelper.setGpsSpeed( ((ParamsInt)params).GetValue() );
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return new ParamsInt(args);
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wca_hc_set_speed));
    }
}

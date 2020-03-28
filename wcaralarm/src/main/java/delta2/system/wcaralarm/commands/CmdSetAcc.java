package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsFloat;
import delta2.system.common.execmd.ParamsInt;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.R;

public class CmdSetAcc extends ExeBaseCmd {

    public CmdSetAcc(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "set acc ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        PreferencesHelper.setAccLevel( ((ParamsInt)params).GetValue() );
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return new ParamsFloat(args);
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wca_hc_set_acc));
    }
}

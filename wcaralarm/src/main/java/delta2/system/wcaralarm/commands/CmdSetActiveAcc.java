package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsInt;
import delta2.system.common.execmd.ParamsOnOff;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.R;

public class CmdSetActiveAcc extends ExeBaseCmd {

    public CmdSetActiveAcc(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "set active acc ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        PreferencesHelper.setIsAccActive( ((ParamsOnOff)params).GetIsOn() );
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return new ParamsOnOff(args);
    }

    @Override
    public String GetHelp() {
        return String.format("%s on|off - %s", GetCommandText(), context.getString(R.string.wca_hc_set_active_acc));
    }
}

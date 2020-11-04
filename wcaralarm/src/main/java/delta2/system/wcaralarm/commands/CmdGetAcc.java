package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsFloat;
import delta2.system.common.execmd.ParamsInt;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.R;

public class CmdGetAcc extends ExeBaseCmd {

    public CmdGetAcc(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "get acc ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        return String.format("%s = %s", context.getString(R.string.wca_acc_info), PreferencesHelper.getAccLevel());
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wca_hc_set_acc));
    }
}

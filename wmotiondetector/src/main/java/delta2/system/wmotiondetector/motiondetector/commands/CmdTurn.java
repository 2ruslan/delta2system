package delta2.system.wmotiondetector.motiondetector.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdTurn extends ExeBaseCmd {

    public CmdTurn(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return "turn";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        PreferencesHelper.SetIsActive(!PreferencesHelper.GetIsActive());
        return null;
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wmd_hc_turn));
    }

}

package delta2.system.wmotiondetector.motiondetector.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wmotiondetector.R;

public class CmdInfo extends ExeBaseCmd {

    public CmdInfo(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "info";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        return InfoHelper.GetInfo(context);
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(),  context.getString(R.string.wmd_hc_info));
    }

}

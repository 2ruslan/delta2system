package delta2.system.wtimer.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsInt;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wtimer.R;
import delta2.system.wtimer.timers.TimerManager;

public class CmdDelAt extends ExeBaseCmd {

    public CmdDelAt(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "del at ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        TimerManager.GetInstance(sender).DelTimer( ((ParamsInt)params).GetValue() );
        return "";
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return new ParamsInt(args);
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wat_add_at));
    }
}

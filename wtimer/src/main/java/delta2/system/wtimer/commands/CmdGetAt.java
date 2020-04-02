package delta2.system.wtimer.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;

import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wtimer.R;
import delta2.system.wtimer.timers.TimerManager;

public class CmdGetAt extends ExeBaseCmd {

    public CmdGetAt(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "get at";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        return GetData();
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wat_get_at));
    }

    private String GetData() {
        StringBuilder sb = new StringBuilder();

        int idx = 0;
        for( String cmd : TimerManager.GetInstance(sender).GetRawCommands())
            sb.append(String.format("[%s] %s", idx++, cmd ));

        return sb.toString();
    }


}

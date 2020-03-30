package delta2.system.delta2system.commands;

import android.content.Context;

import delta2.system.common.Log.L;
import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageFile;
import delta2.system.delta2system.BuildConfig;
import delta2.system.delta2system.InfoData;
import delta2.system.delta2system.MainService;
import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.R;

public class CmdGetLog extends ExeBaseCmd {

    public CmdGetLog(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "get log";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return false;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        sender.RequestSendMessage( new MessageFile(msgId, L._LOG_PATH));

        return "";
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(),  context.getString(R.string.app_hc_get_log));
    }
}

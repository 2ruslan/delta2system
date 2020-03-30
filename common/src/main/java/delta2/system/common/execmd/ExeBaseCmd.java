package delta2.system.common.execmd;

import android.content.Context;

import delta2.system.common.Log.L;
import delta2.system.common.interfaces.messages.IRequestSendMessage;

public abstract class ExeBaseCmd {

    protected static final EmptyCmdParams EmptyCmdParams = new EmptyCmdParams();

    protected Context context;
    protected IRequestSendMessage sender;

    public ExeBaseCmd(Context c, IRequestSendMessage s){
        context = c;
        sender = s;
    }

    protected abstract String GetCommandText();

    protected abstract boolean IsNeedAnswer();

    protected abstract String RunCommand(ICmdParams params, String msgId);

    protected abstract ICmdParams ParseParams(String args);

    public abstract String GetHelp();

    private int lengthCommandText = 0;
    private int GetLengthCommandText(){
        if (lengthCommandText == 0)
            lengthCommandText = GetCommandText().length();
        return lengthCommandText;
    }

    public ExeCmdResult Run(String cmd, String msgId){
        if (cmd.startsWith(GetCommandText())) {

            ICmdParams params = null;

            try {
                params = ParseParams(cmd.substring(GetLengthCommandText()));
            }catch (Exception e){
                L.log.error("", e);
                return new ExeCmdResult(ExeCmdResult.enState.error, e.getMessage(), IsNeedAnswer());
            }

            try {
                return new ExeCmdResult(ExeCmdResult.enState.ok, RunCommand(params, msgId), IsNeedAnswer());
            } catch (Exception e) {
                L.log.error("", e);
                return new ExeCmdResult(ExeCmdResult.enState.error, e.getMessage(), IsNeedAnswer());
            }
        }

        return ExeCmdResult.None;
    }

    protected static class EmptyCmdParams implements ICmdParams {}
}

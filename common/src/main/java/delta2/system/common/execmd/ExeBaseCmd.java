package delta2.system.common.execmd;

import android.content.Context;

import delta2.system.common.Log.L;

public abstract class ExeBaseCmd {

    protected static final EmptyCmdParams EmptyCmdParams = new EmptyCmdParams();

    protected Context context;

    public ExeBaseCmd(Context c){
        context = c;
    }

    protected abstract String GetCommandText();

    protected abstract boolean IsNeedAnswer();

    protected abstract String RunCommand(ICmdParams params);

    protected abstract ICmdParams ParseParams(String args);

    public abstract String GetHelp();

    private int lengthCommandText = 0;
    private int GetLengthCommandText(){
        if (lengthCommandText == 0)
            lengthCommandText = GetCommandText().length();
        return lengthCommandText;
    }

    public ExeCmdResult Run(String cmd){
        if (cmd.startsWith(GetCommandText())) {

            ICmdParams params = null;

            try {
                params = ParseParams(cmd.substring(GetLengthCommandText()));
            }catch (Exception e){
                L.log.error("", e);
                return new ExeCmdResult(ExeCmdResult.enState.error, e.getMessage(), IsNeedAnswer());
            }

            try {
                return new ExeCmdResult(ExeCmdResult.enState.ok, RunCommand(params), IsNeedAnswer());
            } catch (Exception e) {
                L.log.error("", e);
                return new ExeCmdResult(ExeCmdResult.enState.error, e.getMessage(), IsNeedAnswer());
            }
        }

        return ExeCmdResult.None;
    }

    protected static class EmptyCmdParams implements ICmdParams {}
}

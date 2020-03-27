package delta2.system.common.execmd;

import android.content.Context;

import delta2.system.common.Log.L;

public abstract class ExeBaseCmd {

    protected Context context;

    public ExeBaseCmd(Context c){
        context = c;
    }

    protected abstract String GetCommandText();

    protected abstract boolean IsNeedAnswer();

    protected abstract String RunCommand(String cmd);

    public abstract String GetHelp();

    public ExeCmdResult Run(String cmd){
        if (cmd.startsWith(GetCommandText())) {
            try {
                return new ExeCmdResult(ExeCmdResult.enState.ok, RunCommand(cmd), IsNeedAnswer());
            } catch (Exception e) {
                L.log.error("", e);
                return new ExeCmdResult(ExeCmdResult.enState.error, e.getMessage(), IsNeedAnswer());
            }
        }

        return ExeCmdResult.None;
    }
}

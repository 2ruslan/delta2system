package delta2.system.delta2system.commands;

import android.content.Context;

import delta2.system.common.Log.L;
import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsString;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageText;
import delta2.system.delta2system.PreferencesHelper;
import delta2.system.delta2system.R;

public class CmdSetLogLevel extends ExeBaseCmd {

    public CmdSetLogLevel(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "set log level ";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        setLogLevel( ((ParamsString)params).GetValue());

        return "";
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return new ParamsString(args);
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(),  context.getString(R.string.app_hc_set_log_level));
    }

    private static IMessage setLogLevel(String level){
        String result = "ok";

        level = level.toUpperCase();

        try {
            L.setLogLevel(level);
            PreferencesHelper.setLogLevel(level);
        }
        catch (Exception e){
            result = "no";
        }

        return new MessageText(result);


    }
}

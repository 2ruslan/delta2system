package delta2.system.wmotiondetector.motiondetector.Common;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;

public abstract class CmdBase {

    public final String _ON = "on";
    public final String _OFF = "off";

    public en_type type ;
    public String cmd;

    public CmdBase(en_type tp, String c){
        type = tp;
        cmd = c;
    }

    public enum en_type{
        set,
        get,
        other
    }

    public enum en_result{
        ok,
        exception,
        error_msg
    }

    public ResultCmd exec(String msgId, Context context, String orig, String[] parts){
        ResultCmd res;
        try {
            Helper.Log("Cmd.exec",cmd + " " + orig);
            res = run(msgId, context, orig, parts);

            if(type != en_type.get)
                MediatorMD.OnCommandExcecuted(cmd);

        } catch (Exception ex) {
            Helper.Ex2Log(ex);
            res = new ResultCmd(en_result.exception, ex.getMessage());
        }

        return res;

    }

    public void OnDestroy(){}

    public abstract ResultCmd run(String msgId, Context context, String orig, String[] parts);
}

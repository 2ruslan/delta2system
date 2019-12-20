package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;


public class CmdAutostartGet extends CmdBase {
    public static final String _COMMAND = "autostart";

    public String getDescription(Context context){
        return String.format("\n%s - %s", _COMMAND
                , context.getResources().getString(R.string.cmd_auto_start_get_description));
    }

    public CmdAutostartGet(){
        super(en_type.get, _COMMAND);
    }

    public ResultCmd run(Context context, String ori, String[] parts){
        //MediatorMD.SendCameraProp(parms.msgId, _COMMAND);

        return new ResultCmd();
    }

}
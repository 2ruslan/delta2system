package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;


public class CmdCameraAngleGet extends CmdBase {
    public static final String _COMMAND = "angle";

    public String getDescription(Context context){
        return String.format("\n%s - %s", _COMMAND
                , context.getResources().getString(R.string.cmd_delta_set_description));
    }

    public CmdCameraAngleGet(){
        super(en_type.get, _COMMAND);
    }

    public ResultCmd run(Context context, String ori, String[] parts){
        MediatorMD.SendCameraProp( _COMMAND);

        return new ResultCmd();
    }

}
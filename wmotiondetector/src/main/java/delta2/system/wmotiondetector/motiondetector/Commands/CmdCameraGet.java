package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;

public class CmdCameraGet extends CmdBase {
    public static final String _COMMAND = "camera";

    public CmdCameraGet(){
        super(en_type.get, _COMMAND);
    }

    public ResultCmd run(String msgId, Context context, String ori, String[] parts){
        MediatorMD.SendCameraProp(msgId,  _COMMAND);

        return new ResultCmd();
    }

}
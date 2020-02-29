package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdDeltaGet extends CmdBase {
    public static final String _COMMAND = "delta";

    public CmdDeltaGet(){
        super(en_type.get, _COMMAND);
    }

    public ResultCmd run(String msgId, Context context, String ori, String[] parts){
        MediatorMD.sendText( msgId,_COMMAND + " : " + PreferencesHelper.getDelta ());

        return new ResultCmd();
    }

}
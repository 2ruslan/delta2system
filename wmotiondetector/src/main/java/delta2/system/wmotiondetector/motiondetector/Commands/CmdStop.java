package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdStop extends CmdBase {
    public static final String _COMMAND = "stop";

    public CmdStop(){
        super(en_type.other, _COMMAND);
    }

    public ResultCmd run(String msgId, Context context, String ori, String[] parts){
        PreferencesHelper.SetIsActive(false);
        MediatorMD.SetInfo("status = stop" );
        return new ResultCmd();
    }

}
package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdCameraAngleSet extends CmdBase {
    public static final String _COMMAND = "angle";

    public CmdCameraAngleSet(){
        super(en_type.set, _COMMAND);
    }

    public ResultCmd run(String msgId, Context context, String ori, String[] parts){
        int val = Integer.valueOf(parts[2]);
        PreferencesHelper.setCameraAngleIdx (val);

        return new ResultCmd();
    }

}
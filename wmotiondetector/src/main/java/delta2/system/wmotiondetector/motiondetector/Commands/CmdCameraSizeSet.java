package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdCameraSizeSet extends CmdBase {
    public static final String _COMMAND = "size";

    public CmdCameraSizeSet(){
        super(en_type.set, _COMMAND);
    }

    public ResultCmd run(String msgId, Context context, String ori, String[] parts){
        int val = Integer.valueOf(parts[2]);

        if (val != PreferencesHelper.getCameraSizeIdx() )
            PreferencesHelper.setCameraSizeIdx (val);

        return new ResultCmd();
    }

}
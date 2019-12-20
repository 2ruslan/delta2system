package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdDeltaSet extends CmdBase {
    public static final String _COMMAND = "delta";

    public String getDescription(Context context){
        return String.format("\n%s - %s", _COMMAND
                , context.getResources().getString(R.string.cmd_delta_set_description));
    }

    public CmdDeltaSet(){
        super(en_type.set, _COMMAND);
    }

    public ResultCmd run(Context context, String ori, String[] parts){
        int val = Integer.valueOf(parts[2]);
        PreferencesHelper.setDelta (val);

        return new ResultCmd();
    }

}
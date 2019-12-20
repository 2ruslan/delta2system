package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdStart extends CmdBase {
    public static final String _COMMAND = "start";

    public String getDescription(Context context){
        return String.format("\n%s - %s", _COMMAND
                , context.getResources().getString(R.string.cmd_start_description));
    }

    public CmdStart(){
        super(en_type.other, _COMMAND);
    }

    public ResultCmd run(Context context, String ori, String[] parts){
        PreferencesHelper.SetIsActive(true);

        return new ResultCmd();
    }

}
package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;


public class CmdVoiceCallSet extends CmdBase {
    public static final String _COMMAND = "voicecall";

    public CmdVoiceCallSet(){
        super(en_type.set, _COMMAND);
    }

    public ResultCmd run(String msgId, Context context, String ori, String[] parts){
        if (parts[2].equals(_ON))
            PreferencesHelper.setIsVoiceCall(true);
        else if (parts[2].equals(_OFF))
            PreferencesHelper.setIsVoiceCall(false);

        return new ResultCmd();
    }

}
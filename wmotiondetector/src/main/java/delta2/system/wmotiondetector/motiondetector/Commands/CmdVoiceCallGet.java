package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;

public class CmdVoiceCallGet extends CmdBase {
    public static final String _COMMAND = "voicecall";

    public CmdVoiceCallGet(){
        super(CmdBase.en_type.get, _COMMAND);
    }

    public ResultCmd run(String msgId, Context context, String ori, String[] parts){
       // MediatorMD.sendText( parms.msgId,_COMMAND + " : " + PreferencesHelper.getDelta ());

        return new ResultCmd();
    }

}
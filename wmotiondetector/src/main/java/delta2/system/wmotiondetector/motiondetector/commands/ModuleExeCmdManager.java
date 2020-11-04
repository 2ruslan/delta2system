package delta2.system.wmotiondetector.motiondetector.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.execmd.ExeCmdManager;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wmotiondetector.Module;
import delta2.system.wmotiondetector.R;

public class ModuleExeCmdManager extends ExeCmdManager {

    public ModuleExeCmdManager(Context c, IRequestSendMessage s) {
        super(c, s);
        init(c, s);
    }

    @Override
    protected String GetHelpHeader() {
        return Helper.GetMessageHeader(context.getString(R.string.wmd_module_name), Module._MODULE_CODE);
    }

    private void init(Context c, IRequestSendMessage s){
        Add(new CmdInfo(c, s));

        Add(new CmdStart(c, s));
        Add(new CmdStop(c, s));
        Add(new CmdTurn(c, s));

        Add(new CmdDeltaSet(c, s));
        Add(new CmdDeltaGet(c, s));

        Add(new CmdCameraGet(c, s));
        Add(new CmdCameraSet(c, s));

        Add(new CmdCameraAngleGet(c, s));
        Add(new CmdCameraAngleSet(c, s));

        Add(new CmdCameraSizeGet(c, s));
        Add(new CmdCameraSizeSet(c, s));

        Add(new CmdPhotoGet(c, s));
    }

}

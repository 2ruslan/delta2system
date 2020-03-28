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
        init(c);
    }

    @Override
    protected String GetHelpHeader() {
        return Helper.GetMessageHeader(context.getString(R.string.wmd_module_name), Module._MODULE_CODE);
    }

    private void init(Context c){
        Add(new CmdInfo(c));
        Add(new CmdStart(c));
        Add(new CmdStop(c));
        Add(new CmdTurn(c));
        Add(new CmdDeltaSet(c));
        Add(new CmdDeltaGet(c));

    }

}

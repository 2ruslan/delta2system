package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.execmd.ExeCmdManager;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wcaralarm.Module;
import delta2.system.wcaralarm.R;

public class ModuleExeCmdManager extends ExeCmdManager {

    public ModuleExeCmdManager(Context c, IRequestSendMessage s) {
        super(c, s);
        init(c, s);
    }

    @Override
    protected String GetHelpHeader() {
        return Helper.GetMessageHeader(context.getString(R.string.wca_module_name), Module._MODULE_CODE);
    }

    private void init(Context c, IRequestSendMessage s){
        Add(new CmdInfo(c, s));
        Add(new CmdStart(c, s));
        Add(new CmdStop(c, s));
        Add(new CmdSetSpeed(c, s));
        Add(new CmdSetAcc(c, s));
        Add(new CmdSetActiveAcc(c, s));
        Add(new CmdSetActiveGps(c, s));
        Add(new CmdLoc(c, s));
    }

}

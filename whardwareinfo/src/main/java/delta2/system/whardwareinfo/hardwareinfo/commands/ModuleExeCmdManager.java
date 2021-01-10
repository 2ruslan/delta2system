package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.framework.abstraction.CommandManagerBase;

public class ModuleExeCmdManager extends CommandManagerBase {

    public ModuleExeCmdManager(Context c) {
        Register(new CmdInfo(c));
        Register(new CmdNotifyPower(c));
        Register(new CmdNotifyConnection(c));
    }
/*
    @Override
    protected String GetHelpHeader() {
        return Helper.GetMessageHeader(context.getString(R.string.whi_module_name), Module._MODULE_CODE);
    }
*/
}

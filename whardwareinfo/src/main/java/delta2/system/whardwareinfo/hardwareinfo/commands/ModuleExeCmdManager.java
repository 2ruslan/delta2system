package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.execmd.ExeCmdManager;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.whardwareinfo.Module;
import delta2.system.whardwareinfo.R;

public class ModuleExeCmdManager extends ExeCmdManager {

    public ModuleExeCmdManager(Context c, IRequestSendMessage s) {
        super(c, s);
        init(c);
    }

    @Override
    protected String GetHelpHeader() {
        return Helper.GetMessageHeader(context.getString(R.string.whi_module_name), Module._MODULE_CODE);
    }

    private void init(Context c){
        Add(new CmdInfo(c));
        Add(new CmdNotifyPower(c));
        Add(new CmdNotifyConnection(c));
    }

}

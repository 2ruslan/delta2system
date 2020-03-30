package delta2.system.wsu.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.execmd.ExeCmdManager;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wsu.Module;
import delta2.system.wsu.R;

public class ModuleExeCmdManager extends ExeCmdManager {

    public ModuleExeCmdManager(Context c, IRequestSendMessage s) {
        super(c, s);
        init(c, s);
    }

    @Override
    protected String GetHelpHeader() {
        return Helper.GetMessageHeader(context.getString(R.string.wsu_module_name), Module._MODULE_CODE);
    }

    private void init(Context c, IRequestSendMessage s){
        Add(new CmdReboot(c, s));
        Add(new CmdTop(c, s));
    }

}

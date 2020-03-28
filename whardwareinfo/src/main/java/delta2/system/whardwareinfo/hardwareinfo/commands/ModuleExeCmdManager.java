package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeCmdManager;
import delta2.system.common.interfaces.messages.IRequestSendMessage;

public class ModuleExeCmdManager extends ExeCmdManager {
    public ModuleExeCmdManager(IRequestSendMessage s, Context c) {
        super(s);
        init(c);
    }

    private void init(Context c){
        Add(new CmdInfo(c));
        Add(new CmdNotifyPower(c));
        Add(new CmdNotifyConnection(c));
    }

}

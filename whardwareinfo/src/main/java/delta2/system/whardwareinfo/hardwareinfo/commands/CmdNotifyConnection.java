package delta2.system.whardwareinfo.hardwareinfo.commands;

import android.content.Context;

import delta2.system.framework.abstraction.CommandBase;
import delta2.system.framework.interfaces.IMessage;
import delta2.system.whardwareinfo.R;
import delta2.system.whardwareinfo.hardwareinfo.preferences.PreferencesHelper;

public class CmdNotifyConnection extends CommandBase {

    public CmdNotifyConnection(Context c) {
        super(c);
    }

    @Override
    public String GetCommandText() {
        return "set notify connection ";
    }

    @Override
    public IMessage Run(String params) {
        PreferencesHelper.setNotifyConnection(ParseParamsOnOff(params));
        return CreateOkMessage();
    }

    @Override
    public String GetHelp() {
        return String.format("%s on|off - %s", GetCommandText(), context.getString(R.string.whi_hc_notify_connection));
    }
}

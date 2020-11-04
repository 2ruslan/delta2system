package delta2.system.delta2system.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModule;
import delta2.system.common.messages.MessageText;
import delta2.system.delta2system.BuildConfig;
import delta2.system.delta2system.InfoData;
import delta2.system.delta2system.MainService;
import delta2.system.delta2system.ModuleManager;
import delta2.system.delta2system.PreferencesHelper;
import delta2.system.delta2system.R;
import delta2.system.delta2system.View.main.ModuleAdapter;

public class CmdInfo extends ExeBaseCmd {

    public CmdInfo(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "info";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        return getInfo();
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(),  context.getString(R.string.app_hc_info));
    }

    private String getInfo(){

        StringBuilder sb = new StringBuilder();

        sb.append(context.getString(R.string.app_name ));
        sb.append(String.format("\n\n%s\n", context.getString(R.string.delimiter_line)));

        sb.append(String.format("%s : \n", context.getString(R.string.app_active_modules )));
        for(IModule m : ModuleManager.GetAllModules()){
            if (PreferencesHelper.getIsActiveModule(m.GetModuleID())){
                sb.append(String.format("  [%s] [%s] - %s\n", m.GetShortName(), ModuleAdapter.getStateDescr(context, m.GetModuleState()),  m.GetDescription() ));
            }
        }

        sb.append(String.format("\n%s: %s \n", context.getString(R.string.received), InfoData.GetReceive()));
        sb.append(String.format("%s: %s \n", context.getString(R.string.sended), InfoData.GetSend()));

        sb.append(String.format("\n%s: %s \n", context.getString(R.string.version), BuildConfig.VERSION_NAME));

        sb.append(String.format("\n%s: %s \n", context.getString(R.string.uptime), MainService.getWorkingTime()));

        return sb.toString();
    }
}

package delta2.system.wcaralarm.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wcaralarm.Module;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.R;

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
        return GetInfo();
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wca_hc_info));
    }

    private String GetInfo(){
        StringBuilder sb = new StringBuilder(Helper.GetMessageHeader(context.getString(R.string.wca_module_name), Module._MODULE_CODE));

        sb.append(String.format("\n\n%s : %s" , context.getString(R.string.wca_status)
                ,context.getString(PreferencesHelper.getIsStarted() ?  R.string.wca_started : R.string.wca_stoped )));

        return sb.toString();
    }

}

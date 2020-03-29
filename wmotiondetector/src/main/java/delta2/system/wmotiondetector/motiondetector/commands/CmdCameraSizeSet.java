package delta2.system.wmotiondetector.motiondetector.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.execmd.ParamsInt;
import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class CmdCameraSizeSet extends ExeBaseCmd {

    public static final String _COMMAND = "set size ";

    public CmdCameraSizeSet(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return _COMMAND;
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        PreferencesHelper.setCameraSizeIdx (((ParamsInt)params).GetValue());
        MediatorMD.OnCommandExcecuted(_COMMAND);

        return "";
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return new ParamsInt(args);
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wmd_hc_camera_size_set));
    }

}

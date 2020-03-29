package delta2.system.wmotiondetector.motiondetector.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;

public class CmdCameraGet extends ExeBaseCmd {

    public static final String _COMMAND = "get camera";

    public CmdCameraGet(Context c) {
        super(c);
    }

    @Override
    protected String GetCommandText() {
        return _COMMAND;
    }

    @Override
    protected boolean IsNeedAnswer() {
        return false;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        MediatorMD.SendCameraProp(msgId,  _COMMAND);
        return "";
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wmd_hc_camera_get));
    }

}

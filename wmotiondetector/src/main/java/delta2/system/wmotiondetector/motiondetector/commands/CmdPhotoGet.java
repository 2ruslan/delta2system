package delta2.system.wmotiondetector.motiondetector.commands;

import android.content.Context;

import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.RawPicture;

import delta2.system.wmotiondetector.motiondetector.Detector.SendPhoto;
import delta2.system.wmotiondetector.motiondetector.Mediator.IGetRawPictureCallback;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;

public class CmdPhotoGet extends ExeBaseCmd implements IGetRawPictureCallback {

    RawPicture _currentPict = null;

    public CmdPhotoGet(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return false;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        run(msgId);

        return "";
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return  context.getString(R.string.wmd_hc_info);
    }

    String MsgId;
    public void run(String msgId){
        MsgId = msgId;

        if (_currentPict == null) {
            _currentPict = new RawPicture(this);
        }

        _currentPict.msgId = msgId;

        MediatorMD.GetRawPciture(_currentPict,  false);

    }

    @Override
    public void OnGetRawPicture() {
        SendPhoto.Send(MsgId, context, _currentPict.getJpg(), "", _currentPict.h, _currentPict.w);
    }

}

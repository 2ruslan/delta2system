package delta2.system.wmotiondetector.motiondetector.Commands;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.RawPicture;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Detector.SendPhoto;
import delta2.system.wmotiondetector.motiondetector.Mediator.IGetRawPictureCallback;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;


public class CmdPhotoGet extends CmdBase implements IGetRawPictureCallback {
        public static final String _COMMAND = "";
        RawPicture _currentPict = null;

        public CmdPhotoGet(){
                super(en_type.get, _COMMAND);
        }

        @Override
        public void OnDestroy(){
                super.OnDestroy();
                _context = null;
                _currentPict.OnDestroy();
                _currentPict = null;
        }


        Context _context;
        String MsgId;
        public ResultCmd run(String msgId, Context context, String ori, String[] parts){
                _context = context;
                MsgId = msgId;

                if (_currentPict == null) {
                    _currentPict = new RawPicture(this);
                }

                _currentPict.msgId = msgId;

                MediatorMD.GetRawPciture(_currentPict,  false);

                return new ResultCmd();
        }

        @Override
        public void OnGetRawPicture() {
                SendPhoto.Send(MsgId,_context, _currentPict.getJpg(), "", _currentPict.h, _currentPict.w);
        }
}
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

        public String getDescription(Context context){
                return "";
        }

        public static String GetDescription(Context context){
                return String.format("\n\n%s"
                        , context.getResources().getString(R.string.cmd_get_photo_description));
        }

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
        public ResultCmd run(Context context, String ori, String[] parts){
                _context = context;

                if (_currentPict == null) {
                    _currentPict = new RawPicture(this);
                }

                MediatorMD.GetRawPciture(_currentPict,  false);

                return new ResultCmd();
        }

        @Override
        public void OnGetRawPicture() {
                SendPhoto.Send(_context, _currentPict.getJpg(), "");
        }
}
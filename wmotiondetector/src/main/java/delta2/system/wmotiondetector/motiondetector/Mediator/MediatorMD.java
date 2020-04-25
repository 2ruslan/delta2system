package delta2.system.wmotiondetector.motiondetector.Mediator;

import java.util.ArrayList;
import java.util.List;

import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessagePhoto;
import delta2.system.common.messages.MessageText;
import delta2.system.common.messages.MessageVoiceCall;
import delta2.system.wmotiondetector.Module;
import delta2.system.wmotiondetector.motiondetector.Common.RawPicture;
import delta2.system.wmotiondetector.motiondetector.Detector.CamearaProps;

public class MediatorMD {

    private static IRequestSendMessage requestSendMessage;
    public static void RegisterRequestSendMessage(IRequestSendMessage msg) {
        requestSendMessage = msg;
    }

    //endregion ITransport

    //endregion ICommandCheckMessage

    // region IGetRawPciture
    private  static IGetRawPciture _IGetRawPciture;

    public static void registerGetRawPciture(IGetRawPciture p){
        _IGetRawPciture = p;
    }

    public static void GetRawPciture(RawPicture p, boolean isSmalPict){
        if(_IGetRawPciture != null)
            _IGetRawPciture.GetRawPicture(p, isSmalPict);
    }

    public static void onDestroy(){


    }
    //endregion IGetRawPciture

    //region ICommandExcecuted
    private static List<ICommandExcecuted> _CommandExcecuted = new ArrayList<>();

    public static void registerCommandExcecuted(ICommandExcecuted t){
        _CommandExcecuted.add(t);

    }
    public static void unregisterCommandExcecuted(ICommandExcecuted t){
        _CommandExcecuted .remove(t);
    }

    public static void OnCommandExcecuted( String cmd){
        if(_CommandExcecuted != null) {
            for (ICommandExcecuted c :  _CommandExcecuted)
                c.OnCommandExcecuted(cmd);
        }
    }

    //endregion ICommandExcecuted

    // region IGetCameraProp
    private  static IGetCameraProp _GetCameraProp;

    public static void setCameraProp(IGetCameraProp p){
        _GetCameraProp = p;
    }

    public static void SendCameraProp(String msgId, String p){
        if(_GetCameraProp != null)
            _GetCameraProp.SendCameraProp(msgId, p);
    }

    public static CamearaProps GetCameraProps(String prop){
        if(_GetCameraProp != null)
            return  _GetCameraProp.GetCameraProps(prop);
        else
            return null;
    }

    //endregion IGetCameraProp


    // region IGetCameraProp
    private  static ICameraStarted _CameraStarted;

    public static void setOnCameraStarted(ICameraStarted p){
        _CameraStarted = p;
    }

    public static void OnCameraStartted(boolean isStarted){
        if(_CameraStarted != null)
            _CameraStarted.OnCameraStartted (isStarted);
    }

    //endregion IGetCameraProp



    public static void  sendText(String msgId, String msg) {
        requestSendMessage.RequestSendMessage(new MessageText(msgId, msg));
    }

    public static void  sendPhoto(String msgId, String fileUri, String caption, int h, int w) {
        requestSendMessage.RequestSendMessage(new MessagePhoto(Module._MODULE_CODE, msgId, caption, fileUri, h, w));
    }

    public static void  callVoice() {
        requestSendMessage.RequestSendMessage(new MessageVoiceCall(Module._MODULE_CODE, IMessage._NO_MSG_ID));
    }


    private static String _info = "";
    public static void SetInfo(String i){
        _info = i;
    }

    public static String GetInfo(){
        return  _info;
    }

    public static byte GetHowAreYou(){
        return 1;
    }


    static IGetWorkingTime _GetWorkingTime;
    public static void setGetWorkingTime(IGetWorkingTime m){
        _GetWorkingTime = m;
    }

    public static long GetWorkingTime(){
        if(_GetWorkingTime != null)
            return _GetWorkingTime.getWorkingTime();
        return 0;
    }

}


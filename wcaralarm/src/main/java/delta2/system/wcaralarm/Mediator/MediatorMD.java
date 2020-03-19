package delta2.system.wcaralarm.Mediator;


import java.util.ArrayList;
import java.util.List;


public class MediatorMD {

    public static void OnDestroy(){
        _ModeManager.OnDestroy();
    }

    //region ITransport
    private static ITransport _Transport;

    public static void setTransport(ITransport t){
        _Transport = t;
    }

    public static void sendText(String replMsgId, String txt){
        if(_Transport != null)
            _Transport.sendTxt(replMsgId, txt);
    }

    public static void sendLocation(String replMsgId, String lat, String lon){
        if(_Transport != null)
            _Transport.sendLocation(replMsgId, lat, lon);
    }

    public static void sendPhoto(String replMsgId, String file, String caption){
        if(_Transport != null)
            _Transport.sendPhoto(replMsgId, file, caption);
    }

    public static void sendFile(String replMsgId, String file){
        if(_Transport != null)
            _Transport.sendFile(replMsgId, file);
    }

    public static void callVoice(){
        if(_Transport != null)
            _Transport.callVoice();
    }


    //endregion ITransport

    //region ICommandCheckMessage
    private static ICommandCheckMessage _CommandCheckMessage;

    public static void setCommandCheckMessage(ICommandCheckMessage t){
        _CommandCheckMessage = t;
    }
    public static void CheckMessage(String inTxt, boolean repeatCmd, boolean isSilent, String msgId){
        if(_CommandCheckMessage != null)
            _CommandCheckMessage.CheckMessage(inTxt, repeatCmd, isSilent, msgId);
    }
    public static void CheckMessage(String inTxt, String msgId){
        if(_CommandCheckMessage != null)
            _CommandCheckMessage.CheckMessage(inTxt, msgId);
    }

    //endregion ICommandCheckMessage

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


    //region IModeManager
    private static IModeManager _ModeManager;

    public static void setModeManager(IModeManager t){
        _ModeManager = t;
    }

    public static void setSpeed(int speed){
        if(_ModeManager != null)
            _ModeManager.setSpeed(speed);
    }

    public static void  setAcceleration(float acceleration){
        if(_ModeManager != null)
            _ModeManager.setAcceleration(acceleration);
    }

    public static boolean isSendGps(){
        boolean res = false;
        if(_ModeManager != null)
            res = _ModeManager.isSendGps();

        return res;
    }

    public static boolean isSendAcc(){
        boolean res = false;
        if(_ModeManager != null)
            res = _ModeManager.isSendAcc();

        return res;
    }

    //endregion IModeManager

}


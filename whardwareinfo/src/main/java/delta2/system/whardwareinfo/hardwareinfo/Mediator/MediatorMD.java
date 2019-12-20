package delta2.system.whardwareinfo.hardwareinfo.Mediator;


import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;


public class MediatorMD {

    private static IRequestSendMessage messageSender;
    public static void RegisterRequestSendMessage(IRequestSendMessage msg) {
        messageSender = msg;
    }

    public static void RequestSendMessage(IMessage msg){
        messageSender.RequestSendMessage(msg);
    }

    public static void destroy(){
        messageSender = null;
    }
}


package delta2.system.framework.common;

import delta2.system.framework.base.MessageCommand;
import delta2.system.framework.base.MessageFile;
import delta2.system.framework.base.MessageLocation;
import delta2.system.framework.base.MessagePhoto;
import delta2.system.framework.base.MessageText;
import delta2.system.framework.interfaces.IMessageCommand;
import delta2.system.framework.interfaces.IMessageSendFile;
import delta2.system.framework.interfaces.IMessageSendLocation;
import delta2.system.framework.interfaces.IMessageSendPhoto;
import delta2.system.framework.interfaces.IMessageSendText;

public class MessageFactory {

    public static IMessageCommand GetMessageCommand(String command){
        return new MessageCommand(command);
    }

    public static IMessageSendFile GetMessageSendFile(String file, String description){
        return new MessageFile(file, description);
    }

    public static IMessageSendLocation GetMessageSendLocation(String lat, String lon){
        return new MessageLocation(lat, lon);
    }

    public static IMessageSendPhoto GetMessageSendPhoto(byte[] raw, String description){
        return new MessagePhoto(raw, description);
    }

    public static IMessageSendText GetMessageSendText(String text){
        return new MessageText(text);
    }
}

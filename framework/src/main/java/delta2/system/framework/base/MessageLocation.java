package delta2.system.framework.base;

import delta2.system.framework.abstraction.MessageBase;
import delta2.system.framework.interfaces.IMessageSendFile;
import delta2.system.framework.interfaces.IMessageSendLocation;

public class MessageLocation extends MessageBase implements IMessageSendLocation {

    private String lat;
    public String GetLat(){
        return lat;
    }

    private String lon;
    public String GetLon(){
        return lon;
    }

    public MessageLocation(String lt, String ln) {
        lat = lt;
        lon = ln;
    }
}

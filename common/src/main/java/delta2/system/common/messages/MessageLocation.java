package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageLocation implements IMessage {

    private String lat;
    private String lon;

    public MessageLocation(String t, String n){
        lat = t;
        lon = n;
    }

    public String GetLat(){
        return lat;
    }

    public String GetLon(){
        return lon;
    }

}

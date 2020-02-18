package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageLocation implements IMessage {

    private String lat;
    private String lon;

    private String msgId;
    public String getMsgId(){
        return msgId;
    }

    public MessageLocation(String m, String t, String n){
        lat = t;
        lon = n;
        msgId = m;
    }

    public String GetLat(){
        return lat;
    }

    public String GetLon(){
        return lon;
    }

}

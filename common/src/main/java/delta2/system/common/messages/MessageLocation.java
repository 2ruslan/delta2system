package delta2.system.common.messages;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageLocation implements IMessage {

    private String lat;
    private String lon;

    private String msgId;
    private String module;

    public String getMsgId(){
        return msgId;
    }

    @Override
    public String getSrcModule() {
        return module;
    }

    public MessageLocation(String md, String m, String t, String n){
        lat = t;
        lon = n;
        msgId = m;
        module = md;
    }

    public String GetLat(){
        return lat;
    }

    public String GetLon(){
        return lon;
    }

}

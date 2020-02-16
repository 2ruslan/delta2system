package delta2.system.common.messages;

import androidx.annotation.NonNull;

import delta2.system.common.interfaces.messages.IMessage;

public class MessageText implements IMessage {

    private String text;

    public MessageText(String t){
        text = t;
    }

    public String GetText(){
        return text;
    }


    @NonNull
    @Override
    public String toString() {
        return "MessageText.text + " + text;
    }
}

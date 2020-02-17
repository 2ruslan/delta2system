package delta2.system.common.messages;

import androidx.annotation.NonNull;

import delta2.system.common.interfaces.messages.IMessage;

public class MessagePhoto implements IMessage {

    private String caption;
    public String GetCaption(){
        return caption;
    }

    private String file;
    public String GetFile(){
        return file;
    }

    private int Height;
    public int GetHeight(){
        return Height;
    }

    private int Width;
    public int GetWidth(){
        return Width;
    }

    public MessagePhoto(String c, String f, int h, int w){
        caption = c;
        file = f;
        Height = h;
        Width = w;
    }





    @NonNull
    @Override
    public String toString() {
        return "MessageText.caption + " + caption + "; file = " + file;
    }
}

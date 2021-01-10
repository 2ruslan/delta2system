package delta2.system.framework.abstraction;

import android.content.Context;

import delta2.system.framework.common.Log;
import delta2.system.framework.common.MessageFactory;
import delta2.system.framework.interfaces.ICommand;
import delta2.system.framework.interfaces.ILogger;
import delta2.system.framework.interfaces.IMessage;

public abstract class CommandBase implements ICommand {
    protected ILogger logger = Log.Instance();
    protected Context context;

    public CommandBase(Context c){
        context = c;
    }

    protected static String GetMessageHeader( String moduleName, String moduleCode){
        StringBuilder sb = new StringBuilder(String.format("%s [%s]", moduleName, moduleCode));
        sb.append("\n-----------------------------------\n");
        return sb.toString();
    }

    protected IMessage CreateOkMessage() {
        return MessageFactory.GetMessageSendText("ok");
    }

    protected boolean ParseParamsOnOff(String val){
        return val.equals("on");
    }
}

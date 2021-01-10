package delta2.system.framework.abstraction;

import android.content.Context;
import android.content.Intent;

import delta2.system.framework.base.MessageCommand;
import delta2.system.framework.base.MessageFile;
import delta2.system.framework.base.MessageLocation;
import delta2.system.framework.base.MessagePhoto;
import delta2.system.framework.base.MessageText;
import delta2.system.framework.common.Log;
import delta2.system.framework.interfaces.IBus;
import delta2.system.framework.interfaces.ICommandManager;
import delta2.system.framework.interfaces.ILogger;
import delta2.system.framework.interfaces.IMessage;
import delta2.system.framework.interfaces.IMessageCommand;
import delta2.system.framework.interfaces.IMessageSend;
import delta2.system.framework.interfaces.IModule;

public abstract class ModuleBase implements IModule {

    private IBus bus;
    protected Context context;

    protected ILogger logger = Log.Instance();

    private ICommandManager commandManager = GetModuleCommandManager();

    protected abstract ICommandManager GetModuleCommandManager();

    public ModuleBase(Context c, IBus b){
        context = c;
        bus = b;
        bus.RegisterModule(this);
    }

    @Override
    public void OnReceiveMessage(IMessage msg) {
        if (msg instanceof IMessageCommand)
            OnReceiveCommand((IMessageCommand)msg);
        else if (msg instanceof IMessageSend)
            SendMessage(msg);
    }

    private void OnReceiveCommand(IMessageCommand msg){
        IMessage result = commandManager.Run(msg);
        if (result != null)
            SendMessage(result.WithSender(GetModuleName()));
    }

    protected void SendMessage(IMessage msg){
        bus.SendMessage(msg);
    }

    @Override
    public void OpenSettings() {
        try {
            Class<?> cls =  GetSettingsClass();
            if (cls != null) {
                Intent s = new Intent(context, cls);
                s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(s);
            }
        }
        catch (Exception ex){
            logger.error(ex);
        }
    }

    protected abstract Class<?>  GetSettingsClass();
}

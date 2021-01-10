package delta2.system.framework.base;

import java.util.ArrayList;

import delta2.system.framework.common.Log;
import delta2.system.framework.interfaces.IBus;
import delta2.system.framework.interfaces.ILogger;
import delta2.system.framework.interfaces.IMessage;
import delta2.system.framework.interfaces.IModule;

public class Bus implements IBus {
    private ArrayList<IModule> modules = new ArrayList<>();
    private ILogger logger = Log.Instance();

    @Override
    public void RegisterModule(IModule module) {
        modules.add(module);
    }

    @Override
    public void UnregisterModule(IModule module) {
        modules.remove(module);
    }

    @Override
    public void SendMessage(IMessage msg){
        for (IModule m : modules)
            try {
                if ((!m.GetModuleName().equals(msg.GetSenderModuleName())
                        && msg.GetRecipientModuleName().equals(IMessage._NO_VALUE)) ||
                    m.GetModuleName().equals(msg.GetRecipientModuleName()))
                m.OnReceiveMessage(msg);
            }
            catch (Exception e){
                logger.error(e);
            }
    }
}

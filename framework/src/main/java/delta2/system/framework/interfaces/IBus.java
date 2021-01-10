package delta2.system.framework.interfaces;

public interface IBus {
    void RegisterModule(IModule module);
    void UnregisterModule(IModule module);

    void SendMessage(IMessage msg);
}

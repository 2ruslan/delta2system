package delta2.system.framework.interfaces;

public interface ICommandManager {
    IMessage Run(IMessageCommand msg);
}

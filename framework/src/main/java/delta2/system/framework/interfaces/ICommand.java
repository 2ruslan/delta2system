package delta2.system.framework.interfaces;

public interface ICommand {
    String GetCommandText();
    IMessage Run(String params);
    String GetHelp();
}

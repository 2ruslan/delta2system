package delta2.system.framework.interfaces;

import java.util.ArrayList;

public interface IModule extends IMessageReceiver{
    String GetModuleId ();
    String GetModuleName ();
    String GetDescription();

    void OpenSettings();
    ArrayList<String> GetPermissions();

    void Begin();
    void Finish();
}

package delta2.system.common.interfaces.module;

import java.util.ArrayList;

import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.IInit;

public interface IModule extends IInit {

    ArrayList<String> GetAllPermission();

    void LoginAndStart(IAcnivityCallback callback);

    String GetShortName();
    String GetDescription();
    void OpenSettings();

}

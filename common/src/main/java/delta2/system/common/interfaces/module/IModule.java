package delta2.system.common.interfaces.module;

import java.util.ArrayList;

import delta2.system.common.interfaces.IInit;

public interface IModule extends IInit {
    String GetShortName();
    String GetDescription();
    void OpenSettings();
    ArrayList<String> GetAllPermission();
}

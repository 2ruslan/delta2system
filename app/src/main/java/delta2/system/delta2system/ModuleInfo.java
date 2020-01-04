package delta2.system.delta2system;

import delta2.system.common.interfaces.module.IModule;

public class ModuleInfo {

    public ModuleInfo(IModule m){
        name = m.GetShortName();
        info = m.GetDescription();
        isActive = m.GetIsActive();
    }

    public String name;

    public String info;

    public boolean isActive;

}

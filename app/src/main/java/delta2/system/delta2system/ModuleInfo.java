package delta2.system.delta2system;

import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.module.IModule;

public class ModuleInfo {

    public ModuleInfo(IModule m){
        id = m.GetModuleID();
        name = m.GetShortName();
        info = m.GetDescription();
        moduleState = m.GetModuleState();
    }


    private String id;
    public String GetModuleId(){
        return id;
    }

    private String name;
    public String GetName(){
        return name;
    }

    private String info;
    public String GetInfo(){
        return info;
    }

    public ModuleState moduleState;

}

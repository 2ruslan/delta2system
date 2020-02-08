package delta2.system.common.interfaces.module;

import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.IInit;

public interface IModule extends IInit, IError {

    public enum ModuleType{
        worker,
        transport
    }

    String GetModuleID();
    String GetShortName();
    String GetDescription();

    ModuleState GetModuleState();
    void SetOnModuleStateChanged(IModuleStateChanged h);

    ModuleType GetModuleType();

    void OpenSettings();

    void Start();
    void Stop();


    void SetStateNeedInit();

}

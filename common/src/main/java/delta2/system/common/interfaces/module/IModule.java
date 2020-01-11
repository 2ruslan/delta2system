package delta2.system.common.interfaces.module;

import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.IInit;

public interface IModule extends IInit, IError {


    String GetShortName();
    String GetDescription();

    ModuleState GetModuleState();

    void OpenSettings();

    void Start();
    void Stop();
    void Restart();

    String GetModuleID();

}

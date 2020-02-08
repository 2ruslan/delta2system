package delta2.system.delta2system;

import java.util.concurrent.CopyOnWriteArrayList;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.module.IModule;
import delta2.system.common.interfaces.module.IModuleStateChanged;

public class ModulesList extends CopyOnWriteArrayList<IModule> {

    private ModuleState moduleState = ModuleState.none;

    public ModuleState getModuleState() {
        return moduleState;
    }


    public void addModule(IModule m) {
        this.add(m);

        m.SetOnModuleStateChanged(new IModuleStateChanged() {
            @Override
            public void OnChanged() {
                recalcState();
            }
        });

        recalcState();
    }

    private void recalcState() {

        boolean is_initNeed = false;
        boolean is_initBegin = false;
        boolean is_initFinish = false;
        boolean is_startNeed = false;
        boolean is_startBegin = false;


        for (IModule m : this) {
            switch (m.GetModuleState()) {
                case initNeed:
                    is_initNeed = true;
                    break;
                case initBegin:
                    is_initBegin = true;
                    break;
                case initFinish:
                    is_initFinish = true;
                    break;
                case startNeed:
                    is_startNeed = true;
                    break;
                case startBegin:
                    is_startBegin = true;
                    break;
            }
        }

        // priority 0
        if (is_startBegin || is_initBegin || is_initFinish)
            moduleState = ModuleState.wait;
            // priority 1
        else if (is_startNeed)
            moduleState = ModuleState.startNeed;
            // priority 2
        else if (is_initNeed)
            moduleState = ModuleState.startNeed;
            // priority 3
        else
            moduleState = ModuleState.none;
    }
}


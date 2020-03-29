package delta2.system.wtimer;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Constants;
import delta2.system.common.Helper;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModuleStateChanged;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.common.permission.CheckPermission;
import delta2.system.wtimer.commands.ModuleExeCmdManager;

public class Module implements IModuleWorker, IError {

    public static final String _MODULE_CODE = "wat";

    IRequestSendMessage requestSendMessage;

    ModuleExeCmdManager exeCmdManager;

    Context context;

    private IModuleStateChanged moduleStateChanged;

    private ModuleState moduleState;

    private void setModuleState(ModuleState s){
        moduleState = s;

        if (moduleStateChanged != null)
            moduleStateChanged.OnChanged();
    }

    @Override
    public ModuleState GetModuleState() {
        return moduleState;
    }


    public Module(Context c){
        context = c;
        setModuleState(ModuleState.none);
    }

    @Override
    public String GetModuleID() {
        return "7f57efba-204c-4e59-b38a-e1482965fe7c";
    }

    @Override
    public String GetShortName() {
        return _MODULE_CODE;
    }

    @Override
    public String GetDescription() {
        return context.getResources().getString(R.string.wat_module_name);
    }

    @Override
    public void SetOnModuleStateChanged(IModuleStateChanged h) {
        moduleStateChanged = h;
    }

    @Override
    public ModuleType GetModuleType() {
        return ModuleType.worker;
    }

    @Override
    public void SetStateNeedInit() {
        setModuleState(ModuleState.initNeed);
    }
    @Override
    public void RegisterRequestSendMessage(IRequestSendMessage msg) {
        requestSendMessage = msg;
    }

    @Override
    public void ExecuteCommand(ICommand cmd) {
        try {
            if (exeCmdManager != null)
                exeCmdManager.Run(cmd);
        }
        catch (Exception ex){
            OnError(ex);
        }
    }

    @Override
    public void OpenSettings() {
        try {
            Intent s = new Intent(context, SettingsActivity.class);
            s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(s);
        }
        catch (Exception ex){
            OnError(ex);
        }
    }

    @Override
    public void Start() {
        setModuleState(ModuleState.work);
    }

    @Override
    public void Stop() {
        setModuleState(ModuleState.stop);
    }

    @Override
    public void OnError(Exception ex){
        Helper.Ex2Log(ex);
        setModuleState(ModuleState.error);
    }

    @Override
    public void init() {
        setModuleState(ModuleState.initBegin);

        CheckPermission p = new CheckPermission(context, this);
        p.SetOnChecked(
                        new CheckPermission.ICheckedPermission(){
                               @Override
                               public void OnChecked(boolean IsOk) {
                                    if (IsOk && initVars()) {
                                        setModuleState(ModuleState.initFinish);
                                        setModuleState(ModuleState.startNeed);
                                    }
                                    else
                                        setModuleState(ModuleState.error);
                               }
                        });
        p.Check(
                new ArrayList<String>(){{
                    add(Constants._ROOT_PERMISSION);
                }}
        );
    }

    private boolean initVars(){
        try {

            exeCmdManager = new ModuleExeCmdManager(context, requestSendMessage);


            return true;
        }
        catch (Exception ex){
            OnError(ex);
            return false;
        }
    }

    @Override
    public void destroy() {
    }
}
package delta2.system.wcaralarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Helper;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModuleStateChanged;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.common.permission.CheckPermission;
import delta2.system.wcaralarm.Accelerometer.AccelerationManager;
import delta2.system.wcaralarm.GPS.GpsManager;
import delta2.system.wcaralarm.Preferences.PreferencesHelper;
import delta2.system.wcaralarm.commands.ModuleExeCmdManager;


public class Module implements IModuleWorker, IError {

    public static final String _MODULE_CODE = "wca";

    Context context;

    private IModuleStateChanged moduleStateChanged;

    private ModuleState moduleState;

    IRequestSendMessage requestSendMessage;

    ModuleExeCmdManager moduleExeCmdManager;

    CarAlarmManager carAlarmManager;


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
        return "692d0a3b-8774-4a95-bc13-bdc7c93f1886";
    }

    @Override
    public String GetShortName() {
        return _MODULE_CODE;
    }

    @Override
    public String GetDescription() {
        return context.getResources().getString(R.string.wca_module_name);
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
            if (moduleExeCmdManager != null)
                moduleExeCmdManager.Run(cmd);
        }
        catch (Exception ex){
            OnError(ex);
        }
    }

    @Override
    public void OpenSettings() {
        try {
            Intent s = new Intent(context, SettingsCarAlarmActivity.class);
            s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(s);
        }
        catch (Exception ex){
            OnError(ex);
        }
    }

    @Override
    public void Start() {
        carAlarmManager.start();

        setModuleState(ModuleState.work);
    }

    @Override
    public void Stop() {
        carAlarmManager.stop();

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
                    add(Manifest.permission.ACCESS_FINE_LOCATION);
                    add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
                    add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }}
        );
    }

    private boolean initVars(){
        try {
            moduleExeCmdManager = new ModuleExeCmdManager(context, requestSendMessage);
            carAlarmManager = new CarAlarmManager(context, requestSendMessage);

            return true;
        }
        catch (Exception ex){
            OnError(ex);
            return false;
        }
    }

    @Override
    public void destroy() {
        carAlarmManager.destroy();
    }
}
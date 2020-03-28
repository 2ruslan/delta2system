package delta2.system.whardwareinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

import delta2.system.common.Helper;
import delta2.system.common.Log.L;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.execmd.ExeCmdManager;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModuleStateChanged;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.common.permission.CheckPermission;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.BatteryLevelReceiver;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.WifiReceiver;
import delta2.system.whardwareinfo.hardwareinfo.Mediator.MediatorMD;
import delta2.system.whardwareinfo.hardwareinfo.Preferences.PreferencesHelper;
import delta2.system.whardwareinfo.hardwareinfo.SettingsActivity;
import delta2.system.whardwareinfo.hardwareinfo.commands.ModuleExeCmdManager;

public class Module implements IModuleWorker, IError {

    public static final String _MODULE_CODE = "whi";

    IRequestSendMessage requestSendMessage;
    BatteryLevelReceiver batteryLevelReceiver;

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
        return "5628147e-1e1d-4008-b028-cd0f9ee0f4a6";
    }

    @Override
    public String GetShortName() {
        return _MODULE_CODE;
    }

    @Override
    public String GetDescription() {
        return context.getResources().getString(R.string.whi_module_name);
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

        MediatorMD.RegisterRequestSendMessage(msg);
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
                    add(Manifest.permission.ACCESS_NETWORK_STATE);
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    add(Manifest.permission.ACCESS_WIFI_STATE);
                }}
        );
    }

    private boolean initVars(){
        try {
            PreferencesHelper.init(context);

            exeCmdManager = new ModuleExeCmdManager(context, requestSendMessage);

            WifiReceiver.init(context);

            batteryLevelReceiver = new BatteryLevelReceiver();
            BatteryLevelReceiver.init(context);
            context.registerReceiver(batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

            return true;
        }
        catch (Exception ex){
            OnError(ex);
            return false;
        }
    }

    @Override
    public void destroy() {

        WifiReceiver.destroy();

        try {
            if (context != null && batteryLevelReceiver != null)
                context.unregisterReceiver(batteryLevelReceiver);
        }catch (Exception e){
            L.log.error("", e);
        }
        try {
            if (batteryLevelReceiver != null)
                batteryLevelReceiver.destroy();
        }catch (Exception e){
            L.log.error("", e);
        }
        batteryLevelReceiver = null;

        MediatorMD.destroy();
    }
}
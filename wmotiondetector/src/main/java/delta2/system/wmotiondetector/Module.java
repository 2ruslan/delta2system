package delta2.system.wmotiondetector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.util.ArrayList;

import delta2.system.common.Helper;
import delta2.system.common.commands.Command;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.common.permission.CheckPermission;
import delta2.system.wmotiondetector.motiondetector.CommandManager;
import delta2.system.wmotiondetector.motiondetector.Detector.MDManager;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;
import delta2.system.wmotiondetector.motiondetector.SettingsActivity;

public class Module implements IModuleWorker {

    Context context;
    MDManager Manager;

    ModuleState moduleState;

    public Module(Context c){
        context = c;
        moduleState = ModuleState.none;
    }

    @Override
    public void RegisterRequestSendMessage(IRequestSendMessage msg) {
        MediatorMD.RegisterRequestSendMessage(msg);
    }

    @Override
    public void ExecuteCommand(ICommand cmd) {
        if (cmd instanceof Command) {
            MediatorMD.CheckMessage(((Command) cmd).GetCommand());
        }
    }

    @Override
    public String GetModuleID() {
        return "b2deddf0-3917-40d6-9117-9a98c7be0bcc";
    }

    @Override
    public String GetShortName() {
        return "wmd";
    }

    @Override
    public String GetDescription() {
        return "motion detector";
    }

    @Override
    public ModuleState GetModuleState() {
        return moduleState;
    }

    @Override
    public ModuleType GetModuleType() {
        return ModuleType.worker;
    }

    @Override
    public void OpenSettings() {
        Intent s = new Intent(context, SettingsActivity.class);
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(s);
    }

    @Override
    public void Start() {
        moduleState = ModuleState.work;
    }

    @Override
    public void Stop() {
        moduleState = ModuleState.stop;
    }

    @Override
    public void SetStateNeedInit() {
        moduleState = ModuleState.needInit;
    }

    @Override
    public void init() {

        CheckPermission p = new CheckPermission(context, this);
        p.SetOnChecked(
                new CheckPermission.ICheckedPermission(){
                    @Override
                    public void OnChecked(boolean IsOk) {
                        if (IsOk && initVars())
                            moduleState = ModuleState.init;
                        else
                            moduleState = ModuleState.error;
                    }
                });
        p.Check(
                new ArrayList<String>(){{
                    add(Manifest.permission.CAMERA);
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    add(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                }}
        );
    }

    private boolean initVars(){
        try {
            PreferencesHelper.init(context);

            Manager = new MDManager(context);
            MediatorMD.setCommandCheckMessage(new CommandManager(context));

            return true;
        }
        catch (Exception ex){
            OnError(ex);
            return false;
        }
    }

    @Override
    public void destroy() {
        MediatorMD.onDestroy();
        Manager.onDestroy();
        moduleState = ModuleState.none;
    }

    @Override
    public void OnError(Exception ex) {
        Helper.Ex2Log(ex);
        moduleState = ModuleState.error;
    }
}

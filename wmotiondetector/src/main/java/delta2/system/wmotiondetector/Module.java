package delta2.system.wmotiondetector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.util.ArrayList;

import delta2.system.common.Constants;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.wmotiondetector.motiondetector.CommandManager;
import delta2.system.wmotiondetector.motiondetector.Detector.MDManager;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;
import delta2.system.wmotiondetector.motiondetector.SettingsActivity;


public class Module implements IModuleWorker {

    Context context;
    MDManager Manager;

    private boolean isActive = false;

    public Module(Context c){
        context = c;
    }

    @Override
    public void RegisterRequestSendMessage(IRequestSendMessage msg) {
        MediatorMD.RegisterRequestSendMessage(msg);
    }

    @Override
    public void ExecuteCommand(ICommand cmd) {

    }

    @Override
    public String GetShortName() {
        return null;
    }

    @Override
    public String GetDescription() {
        return null;
    }

    @Override
    public boolean GetIsActive() {
        return false;
    }

    @Override
    public void OpenSettings() {
        Intent s = new Intent(context, SettingsActivity.class);
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(s);
    }

    @Override
    public ArrayList<String> GetAllPermission() {
        return new ArrayList<String>(){{
            add(Manifest.permission.CAMERA);
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            add(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        }};
    }

    @Override
    public void LoginAndStart(IAcnivityCallback callback) {
        callback.OnActivityCallback(new Intent().putExtra(Constants._LOGIN_AND_START, true));
        isActive = true;
    }

    @Override
    public String GetModuleID() {
        return "b2deddf0-3917-40d6-9117-9a98c7be0bcc";
    }

    @Override
    public void init() {
        PreferencesHelper.init(context);

        Manager = new MDManager(context);
        MediatorMD.setCommandCheckMessage(new CommandManager(context));
    }

    @Override
    public void destroy() {
        MediatorMD.onDestroy();
        Manager.onDestroy();
        isActive = false;
    }
}

package delta2.system.whardwareinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;

import java.util.ArrayList;

import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.whardwareinfo.hardwareinfo.CommandManager;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.BatteryLevelReceiver;
import delta2.system.whardwareinfo.hardwareinfo.Hardware.WifiReceiver;
import delta2.system.whardwareinfo.hardwareinfo.Mediator.MediatorMD;
import delta2.system.whardwareinfo.hardwareinfo.Preferences.PreferencesHelper;
import delta2.system.whardwareinfo.hardwareinfo.SettingsActivity;

public class Module implements IModuleWorker {

    BatteryLevelReceiver batteryLevelReceiver;
    CommandManager commandManager;
    Context context;

    public Module(Context c){
        context = c;
    }

    @Override
    public void RegisterRequestSendMessage(IRequestSendMessage msg) {
        MediatorMD.RegisterRequestSendMessage(msg);
    }

    @Override
    public void ExecuteCommand(ICommand cmd) {
        commandManager.ExcuteCommand(cmd);
    }

    @Override
    public String GetShortName() {
        return "whi";
    }

    @Override
    public String GetDescription() {
        return "";
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
            add(Manifest.permission.ACCESS_NETWORK_STATE);
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            add(Manifest.permission.ACCESS_WIFI_STATE);
        }};
    }


    @Override
    public void init() {
        PreferencesHelper.init(context);

        WifiReceiver.init(context);

        batteryLevelReceiver = new BatteryLevelReceiver();
        BatteryLevelReceiver.init(context);
        context.registerReceiver(batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        context.registerReceiver(batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));

        commandManager = new CommandManager(context);
    }

    @Override
    public void destroy() {
        commandManager.destroy();

        WifiReceiver.destroy();


        context.unregisterReceiver(batteryLevelReceiver);
        batteryLevelReceiver.destroy();
        batteryLevelReceiver = null;

        MediatorMD.destroy();
    }
}

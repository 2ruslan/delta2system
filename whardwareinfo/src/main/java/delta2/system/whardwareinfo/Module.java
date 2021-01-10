package delta2.system.whardwareinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

import delta2.system.framework.abstraction.ModuleBase;
import delta2.system.framework.interfaces.IBus;
import delta2.system.framework.interfaces.ICommandManager;
import delta2.system.whardwareinfo.hardwareinfo.hardware.BatteryLevelReceiver;
import delta2.system.whardwareinfo.hardwareinfo.hardware.WifiReceiver;

import delta2.system.whardwareinfo.hardwareinfo.SettingsActivity;
import delta2.system.whardwareinfo.hardwareinfo.commands.ModuleExeCmdManager;

public class Module extends ModuleBase {

    public static final String _MODULE_CODE = "whi";

    BatteryLevelReceiver batteryLevelReceiver;

    ModuleExeCmdManager exeCmdManager;

    Context context;

    @Override
    public String GetModuleId() {
        return "5628147e-1e1d-4008-b028-cd0f9ee0f4a6";
    }

    @Override
    public String GetModuleName() {
        return "whi";
    }

    @Override
    public String GetDescription() {
        return context.getResources().getString(R.string.whi_module_name);
    }

    @Override
    public ArrayList<String> GetPermissions() {
        return new ArrayList<String>(){{
            add(Manifest.permission.ACCESS_NETWORK_STATE);
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            add(Manifest.permission.ACCESS_WIFI_STATE);
        }};
    }

    @Override
    protected ICommandManager GetModuleCommandManager() {
        return new ModuleExeCmdManager(context);
    }

    @Override
    protected Class<?> GetSettingsClass() {
        return SettingsActivity.class;
    }

    public Module(Context c, IBus b){
        super(c, b);
        context = c;
    }

    @Override
    public void Begin(){
        try {
            exeCmdManager = new ModuleExeCmdManager(context);

            WifiReceiver.init(context, this);

            batteryLevelReceiver = new BatteryLevelReceiver();
            BatteryLevelReceiver.init(context, this);
            context.registerReceiver(batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
        catch (Exception ex){
            logger.error(ex);
        }
    }

    @Override
    public void Finish() {

        WifiReceiver.destroy();

        try {
            if (context != null && batteryLevelReceiver != null)
                context.unregisterReceiver(batteryLevelReceiver);
                batteryLevelReceiver.destroy();
        }catch (Exception e){
            logger.error(e);
        }

        batteryLevelReceiver = null;
    }


}
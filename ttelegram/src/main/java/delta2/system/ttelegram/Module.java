package delta2.system.ttelegram;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.ttelegram.transporttelegram.Preferences.PreferencesHelper;
import delta2.system.ttelegram.transporttelegram.SettingsActivity;
import delta2.system.ttelegram.transporttelegram.Transport.TelegramTransport;

public class Module implements IModuleTransport {

    Context context;
    TelegramTransport transport;

    private boolean isActive = false;

    public Module(Context c){
        context = c;
    }

    @Override
    public void RegisterReceiveMessage(IReceiveMessage rcv) {
        transport.RegisterReceiveMessage(rcv);
    }

    @Override
    public void SendMessage(IMessage msg) {
        transport.SendMessage(msg);
    }

    @Override
    public String GetShortName() {
        return "ttm";
    }

    @Override
    public String GetDescription() {
        return "";
    }

    @Override
    public boolean GetIsActive() {
        return isActive;
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
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }};
    }

    @Override
    public void init() {
        PreferencesHelper.init(context);
        transport = new TelegramTransport(context);
    }

    @Override
    public void LoginAndStart(IAcnivityCallback callback) {
        transport.setAcnivityCallback(callback);
        transport.connect();
        isActive = true;
    }

    @Override
    public void destroy() {
        transport.destroy();
        isActive = false;
    }
}

package delta2.system.tdropbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.tdropbox.transportdropbox.Preferences.PreferencesHelper;
import delta2.system.tdropbox.transportdropbox.SettingsActivity;
import delta2.system.tdropbox.transportdropbox.Transport.DropBoxTransport;

public class Module implements IModuleTransport {

    private DropBoxTransport transport;
    private Context context;

    public Module(Context c){
        context = c;
    }

    @Override
    public void RegisterReceiveMessage(IReceiveMessage rcv) {

    }

    @Override
    public void SendMessage(IMessage msg) {
        transport.SendMessage(msg);
    }

    @Override
    public String GetShortName() {
        return "tdb";
    }

    @Override
    public String GetDescription() {
        return null;
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

        transport = new DropBoxTransport(context);
        transport.init();
    }

    @Override
    public void destroy() {
        transport.destroy();
        transport = null;

        PreferencesHelper.destroy();
    }
}

package delta2.system.tdropbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Helper;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.common.permission.CheckPermission;
import delta2.system.tdropbox.transportdropbox.Preferences.PreferencesHelper;
import delta2.system.tdropbox.transportdropbox.SettingsActivity;
import delta2.system.tdropbox.transportdropbox.Transport.DropBoxTransport;
import delta2.system.tdropbox.transportdropbox.Transport.Login;

public class Module implements IModuleTransport, IError {

    private DropBoxTransport transport;
    private Context context;
    private ModuleState moduleState;

    public Module(Context c){
        context = c;
        moduleState = ModuleState.none;
    }

    @Override
    public String GetModuleID() {
        return "eec13a38-8841-45bb-af0c-dff897b4a5d1";
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
    public ModuleState GetModuleState() {
        return moduleState;
    }

    @Override
    public void RegisterReceiveMessage(IReceiveMessage rcv) {

    }

    @Override
    public void SendMessage(IMessage msg) {
        transport.SendMessage(msg);
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
        Login login = new Login(context, this);
        login.SetLoginedHandler(
                new Login.ILoginned() {
                    @Override
                    public void OnLogin(boolean IsOk) {
                        if (IsOk && ProcessingStart())
                            moduleState = ModuleState.work;
                        else
                            moduleState = ModuleState.error;
                    }
                }
        );

        login.Check();
    }

    private boolean ProcessingStart(){
        try {
            transport = new DropBoxTransport(context);
            transport.init();

            return true;
        }catch (Exception ex){
            OnError(ex);
            return false;
        }
    }

    @Override
    public void Stop() {
        moduleState = ModuleState.stop;
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
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }}
        );
    }

    private boolean initVars(){
        try {
            PreferencesHelper.init(context);

            return true;
        }
        catch (Exception ex){
            OnError(ex);
            return false;
        }
    }

    @Override
    public void destroy() {
        transport.destroy();
        transport = null;

        PreferencesHelper.destroy();
    }

    @Override
    public void OnError(Exception ex) {
        Helper.Ex2Log(ex);
        moduleState = ModuleState.error;
    }
}

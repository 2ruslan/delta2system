package delta2.system.tline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Helper;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.module.IModuleStateChanged;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.common.permission.CheckPermission;
import delta2.system.tline.Preferences.PreferencesHelper;
import delta2.system.tline.Transport.LineTransport;

public class Module implements IModuleTransport, IError {

    private LineTransport transport;
    private Context context;
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
        return "053d1a95-9a56-4417-ad01-236759671d2a";
    }

    @Override
    public String GetShortName() {
        return "tdb";
    }

    @Override
    public String GetDescription() {
        return context.getResources().getString(R.string.tln_module_name);
    }


    @Override
    public void SetOnModuleStateChanged(IModuleStateChanged h) {
        moduleStateChanged = h;
    }

    @Override
    public ModuleType GetModuleType() {
        return ModuleType.transport;
    }

    @Override
    public void SetStateNeedInit() {
        setModuleState(ModuleState.initNeed);
    }

    @Override
    public void RegisterReceiveMessage(IReceiveMessage rcv) {

    }

    @Override
    public void SendMessage(IMessage msg) {
       // transport.SendMessage(msg);
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
        setModuleState(ModuleState.startBegin);

        Login login = new Login(context, this);
        login.SetLoginedHandler(
                new Login.ILoginned() {
                    @Override
                    public void OnLogin(boolean IsOk) {
                        if (IsOk && ProcessingStart())
                            setModuleState(ModuleState.work);
                        else
                            setModuleState(ModuleState.error);
                    }
                }
        );

        login.Check();
    }

    private boolean ProcessingStart(){
        try {
            transport = new LineTransport(context);
            transport.init();

            return true;
        }catch (Exception ex){
            OnError(ex);
            return false;
        }
    }

    @Override
    public void Stop() {
        setModuleState(ModuleState.stop);
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
        setModuleState(ModuleState.error);
    }


}
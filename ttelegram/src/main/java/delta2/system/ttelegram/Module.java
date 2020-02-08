package delta2.system.ttelegram;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Constants;
import delta2.system.common.Helper;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.module.IModuleStateChanged;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.common.permission.CheckPermission;
import delta2.system.ttelegram.transporttelegram.Preferences.PreferencesHelper;
import delta2.system.ttelegram.transporttelegram.SettingsActivity;
import delta2.system.ttelegram.transporttelegram.Transport.TelegramTransport;

public class Module implements IModuleTransport, IError {

    Context context;
    TelegramTransport transport;

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
    public void RegisterReceiveMessage(IReceiveMessage rcv) {
        transport.RegisterReceiveMessage(rcv);
    }

    @Override
    public void SendMessage(IMessage msg) {
        transport.SendMessage(msg);
    }

    @Override
    public String GetModuleID() {
        return "4568e9c7-f547-41c2-9cd0-8187a4aa32b7";
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
    public void OpenSettings() {
        Intent s = new Intent(context, SettingsActivity.class);
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(s);
    }

    @Override
    public void Start() {
        setModuleState(ModuleState.startBegin);

        transport.setAcnivityCallback(
                new IAcnivityCallback() {
                    @Override
                    public void OnActivityCallback(Intent intent) {
                        if (intent.getBooleanExtra(Constants._LOGIN_AND_START, false)){
                            setModuleState(ModuleState.work);
                        }
                        else
                            setModuleState(ModuleState.error);
                    }
                }
        );
        transport.connect();
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
                        if (IsOk && initVars())
                            setModuleState(ModuleState.initFinish);
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
            transport = new TelegramTransport(context);

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
        moduleState = ModuleState.none;
    }

    @Override
    public void OnError(Exception ex) {
        Helper.Ex2Log(ex);
        moduleState = ModuleState.error;
    }
}

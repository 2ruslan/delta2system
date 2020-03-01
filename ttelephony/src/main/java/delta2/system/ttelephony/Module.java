package delta2.system.ttelephony;

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
import delta2.system.common.messages.MessageForward;
import delta2.system.common.messages.MessageText;
import delta2.system.common.permission.CheckPermission;

import delta2.system.ttelephony.transporttelephony.Preferences.PreferencesHelper;
import delta2.system.ttelephony.transporttelephony.SettingsActivity;
import delta2.system.ttelephony.transporttelephony.Transport.TelephonyTransport;

public class Module implements IModuleTransport, IError {

    private static Module instance;

    private TelephonyTransport transport;
    private Context context;
    private IModuleStateChanged moduleStateChanged;

    private ModuleState moduleState;

    IReceiveMessage reciever;

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
        instance = this;
        setModuleState(ModuleState.none);
    }

    @Override
    public String GetModuleID() {
        return "fbcc4a56-4209-49da-9d5f-a1e525d69032";
    }

    @Override
    public String GetShortName() {
        return "tph";
    }

    @Override
    public String GetDescription() {
        return context.getResources().getString(R.string.tph_module_name);
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
        reciever = rcv;
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
        setModuleState(ModuleState.startBegin);

        if (initVars() && ProcessingStart())
            setModuleState(ModuleState.work);
    }

    private boolean ProcessingStart(){
        try {
            transport = new TelephonyTransport(context);

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
                    add(Manifest.permission.RECEIVE_SMS);
                    add(Manifest.permission.READ_SMS);
                    add(Manifest.permission.SEND_SMS);
                    add(Manifest.permission.CALL_PHONE);
                    add(Manifest.permission.READ_PHONE_STATE);
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
        transport = null;
        instance = null;
    }

    @Override
    public void OnError(Exception ex) {
        Helper.Ex2Log(ex);
        setModuleState(ModuleState.error);
    }

    public static void OnRecieveMsg(String msg){
        if (instance != null && instance.reciever != null)
            instance.reciever.OnReceiveMessage(new MessageText(msg));

    }

    public static void OnResendMsg(String msg){
        if (instance != null && instance.reciever != null)
            instance.reciever.OnReceiveMessage(
                    new MessageForward(new MessageText(msg))
            );
    }

}

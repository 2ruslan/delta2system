package delta2.system.delta2system;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Constants;
import delta2.system.common.Helper;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.IInit;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModule;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.delta2system.View.StarterApp;


public class ModuleManager implements IRequestSendMessage, IReceiveMessage, IInit, IAcnivityCallback {

    Context context;
    ArrayList<IModuleWorker> ModuleWorkers;
    ArrayList<IModuleTransport> ModuleTransports;

    IModule AllModules[];
    IAppCompleteInit AppCompleteInit;

    public ModuleManager(Context c, IAppCompleteInit a){
        context = c;
        AppCompleteInit = a;
    }

    //region init work
    // 1
    public void init()
    {
        Helper.setWorkDir(context.getFilesDir());

        initTransport();
        initWorker();

        if (GetAllModules().size() == 0)
            LoginAndStart();
        else
            checkPermission();
    }

    // 1.1
    private void initTransport(){
        ModuleTransports = new ArrayList<>();
        ModuleTransports.add(new delta2.system.tdropbox.Module(context));
        ModuleTransports.add(new delta2.system.ttelegram.Module(context));
    }

    // 1.2
    private void initWorker(){
        ModuleWorkers = new ArrayList<>();
        ModuleWorkers.add(new delta2.system.whardwareinfo.Module(context));
        ModuleWorkers.add(new delta2.system.wmotiondetector.Module(context));
    }

    //2
    private void checkPermission(){

        ArrayList<String> allPermission = new ArrayList<>();

        for( IModuleWorker worker : ModuleWorkers) {
            if (PreferencesHelper.getIsActiveModule(worker.GetModuleID()))
                allPermission.addAll(worker.GetAllPermission());
        }
        for(IModuleTransport transport : ModuleTransports) {
            if (PreferencesHelper.getIsActiveModule(transport.GetModuleID()))
                allPermission.addAll(transport.GetAllPermission());
        }

        StarterApp.setActivityCallback(this);

        Intent i = new Intent(context, StarterApp.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putStringArrayListExtra(Constants._ALL_PERMISSION, allPermission);
        context.startActivity(i);
    }

    //3
    @Override
    public void OnActivityCallback(Intent intent) {
        if (intent.getBooleanExtra(Constants._ALL_PERMISSION, false)) {
            StarterApp.destroy();

            for (IModuleWorker worker : ModuleWorkers) {
                if (PreferencesHelper.getIsActiveModule(worker.GetModuleID())) {
                    worker.init();
                    worker.RegisterRequestSendMessage(this);
                }
            }

            for (IModuleTransport transport : ModuleTransports) {
                if (PreferencesHelper.getIsActiveModule(transport.GetModuleID())) {
                    transport.init();
                    transport.RegisterReceiveMessage(this);
                }
            }


            allModules = GetAllModules();

            LoginAndStart();
        }
        else if (intent.getBooleanExtra(Constants._LOGIN_AND_START, false)){
            LoginAndStart();
        }
    }

    ArrayList<IModule> allModules;

    // 4
    private void LoginAndStart(){
        if (allModules != null && !allModules.isEmpty()){
            IModule module = allModules.get(0);
            allModules.remove(0);
            module.LoginAndStart(this);
            return;
        }
        else
            AppCompleteInit.OnAppCompleteInit();
    }

    //endregion init work

    @Override
    public void destroy() {
        for(IModuleTransport transport : ModuleTransports) {
            transport.destroy();
        }
        for( IModuleWorker worker : ModuleWorkers) {
            worker.destroy();
        }
    }

    @Override
    public void RequestSendMessage(IMessage msg) {
        MessageExt m = ParseMessage(msg);
        for(IModuleTransport transport : ModuleTransports) {
            if (m.module.equals(MessageExt._ALL) || m.module.equals(transport.GetShortName()))
                transport.SendMessage(msg);
        }
    }

    @Override
    public void OnReceiveMessage(IMessage msg) {
        CommandExt c = ParseCommand(msg);
        for( IModuleWorker worker : ModuleWorkers) {
            if (c.module.equals(CommandExt._ALL) || c.module.equals(worker.GetShortName()))
                worker.ExecuteCommand(c.cmd);
        }
    }

    private CommandExt ParseCommand(IMessage msg){
        return  new CommandExt();
    }

    private MessageExt ParseMessage(IMessage msg){
        return  new MessageExt();
    }

    public ArrayList<IModule> GetAllModules(){
        ArrayList<IModule> result = new ArrayList<>();

        for(IModule module : ModuleTransports)
            if (PreferencesHelper.getIsActiveModule(module.GetModuleID())) {
                result.add(module);
            }
        for(IModule module : ModuleWorkers)
            if (PreferencesHelper.getIsActiveModule(module.GetModuleID())) {
                result.add(module);
            }

        return result;
    }

    public ArrayList<ModuleInfo> GetTransportModules(){
        return GetModulesInfo(ModuleTransports);
    }

    public ArrayList<ModuleInfo> GetWorkerModules(){
        return GetModulesInfo(ModuleWorkers);
    }

    public void Reinit(){
        for(IModule module : ModuleTransports)
            if (module.GetIsActive())
                module.destroy();
        for(IModule module : ModuleWorkers)
            if (module.GetIsActive())
                module.destroy();

        init();

    }

    private ArrayList<ModuleInfo> GetModulesInfo(ArrayList m){
        ArrayList<ModuleInfo> result = new ArrayList<>();
        for(Object module : m)
            result.add(new ModuleInfo((IModule) module));

        return result;
    }

    private class CommandExt{
        public static final String _ALL = "*";
        public ICommand cmd;
        public String module = _ALL;
    }

    private class MessageExt{
        public static final String _ALL = "*";
        public IMessage msg;
        public String module = _ALL;
    }

}

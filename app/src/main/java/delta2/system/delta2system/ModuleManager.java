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
import delta2.system.common.messages.MessageText;


public class ModuleManager implements IRequestSendMessage, IReceiveMessage, IInit, IAcnivityCallback {

    Context context;
    IModuleWorker[] ModuleWorkers;
    IModuleTransport[] ModuleTransports;
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

        checkPermission();
    }

    // 1.1
    private void initTransport(){
        ModuleTransports = new IModuleTransport[]
                {
                        new delta2.system.tdropbox.Module(context),
                        new delta2.system.ttelegram.Module(context)
                } ;
    }

    // 1.2
    private void initWorker(){
        ModuleWorkers = new IModuleWorker[]
                {
                        new delta2.system.whardwareinfo.Module(context),
                        new delta2.system.wmotiondetector.Module(context)
                } ;
    }

    //2
    private void checkPermission(){

        ArrayList<String> allPermission = new ArrayList<>();

        for( IModuleWorker worker : ModuleWorkers) {
            allPermission.addAll(worker.GetAllPermission());
        }
        for(IModuleTransport transport : ModuleTransports) {
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
                worker.init();
                worker.RegisterRequestSendMessage(this);
            }

            for (IModuleTransport transport : ModuleTransports) {
                transport.init();
                transport.RegisterReceiveMessage(this);
            }


            for(IModule module : ModuleTransports)
                allModules.add(module);
            for(IModule module : ModuleWorkers)
                allModules.add(module);

            LoginAndStart();
        }
        else if (intent.getBooleanExtra(Constants._LOGIN_AND_START, false)){
            LoginAndStart();
        }
    }

    ArrayList<IModule> allModules = new ArrayList<>();

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

package delta2.system.delta2system;

import android.content.Context;
import android.content.Intent;
import android.media.midi.MidiOutputPort;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Constants;
import delta2.system.common.Helper;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.IInit;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModule;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.delta2system.View.StarterApp;
import delta2.system.wmotiondetector.Module;


public class ModuleManager implements IRequestSendMessage, IReceiveMessage, IInit, IAcnivityCallback, IError {

    Context context;

    ArrayList<IModule> modules;

    Timer timer;
    CheckStateTimerTask timerTask;

    IModule AllModules[];
    IAppCompleteInit AppCompleteInit;



    public ModuleManager(Context c, IAppCompleteInit a){
        context = c;
        AppCompleteInit = a;

        Helper.setWorkDir(context.getFilesDir());

        initModules();

        initTimerCheckState();
    }


    // region check modules state
    public void checkState(){
        checkModulesInit();
        checkModulesStart();
        checkModulesWork();
    }

    private void checkModulesInit(){
        for(IModule m : modules){
            if (m.GetModuleState() == ModuleState.needInit) {
                try {
                    m.init();

                    if (m instanceof IModuleWorker)
                        ((IModuleWorker)m).RegisterRequestSendMessage(this);
                    else if (m instanceof IModuleTransport)
                        ((IModuleTransport)m).RegisterReceiveMessage(this);

                }
                catch (Exception ex){
                    OnError(ex);
                }
            }
        }
    }

    private void checkModulesStart(){
        boolean isAllInited = true;
        boolean isWorkStart = false;

        for(IModule m : modules){
            ModuleState state = m.GetModuleState();

            if (state == ModuleState.needInit || state == ModuleState.initStart)
                isAllInited = false;

            if (state == ModuleState.workStart)
                isWorkStart = true;
        }

        if (isAllInited || isWorkStart)
            return;

        for(IModule m : modules){
            if (m.GetModuleState() == ModuleState.initEnd){
                m.Start();
                break;
            }
        }

    }

    private void checkModulesWork(){
        boolean isAllWork = true;

        for(IModule m : modules){
            ModuleState state = m.GetModuleState();
            if (state != ModuleState.work && state != ModuleState.error)
                isAllWork = false;
        }

        if (isAllWork){
            AppCompleteInit.OnAppCompleteInit();
        }


    }
    // endregion check modules state


    private void initModules(){
        modules = new ArrayList<>();

        // transport
        modules.add(new delta2.system.tdropbox.Module(context));
        modules.add(new delta2.system.ttelegram.Module(context));

        // worker
        modules.add(new delta2.system.whardwareinfo.Module(context));
        modules.add(new delta2.system.wmotiondetector.Module(context));

        // set need init
        for(IModule m :modules) {
            if (PreferencesHelper.getIsActiveModule(m.GetModuleID()))
                m.SetStateNeedInit();
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
    public void init() {

    }

    @Override
    public void destroy() {
        for(IModule m :modules) {
            m.destroy();
        }
    }


    //region route
    @Override
    public void RequestSendMessage(IMessage msg) {
        MessageExt m = ParseMessage(msg);
        for(IModule module : modules) {
            if (EqualsName(m.module, module)) {
                if (module instanceof IModuleTransport){
                    ((IModuleTransport)module).SendMessage(msg);
                }
            }
        }
    }

    @Override
    public void OnReceiveMessage(IMessage msg) {
        CommandExt c = ParseCommand(msg);
        for( IModule module : modules) {
            if (EqualsName(c.module, module)) {
                if (module instanceof IModuleWorker )
                    ((IModuleWorker)module).ExecuteCommand(c.cmd);
            }
        }
    }

    //endregion route

    private CommandExt ParseCommand(IMessage msg){
        return  new CommandExt();
    }

    private MessageExt ParseMessage(IMessage msg){
        return  new MessageExt();
    }

    private boolean EqualsName(String name,  IModule m){
        return name.equals(MessageExt._ALL) || name.equals(m.GetShortName());
    }

    public ArrayList<IModule> GetAllModules(){
        return modules;
    }

    public ArrayList<ModuleInfo> GetTransportModules(){
        return GetModulesInfo(ModuleTransports);
    }

    public ArrayList<ModuleInfo> GetWorkerModules(){
        return GetModulesInfo(ModuleWorkers);
    }



    private ArrayList<ModuleInfo> GetModulesInfo(ArrayList m){
        ArrayList<ModuleInfo> result = new ArrayList<>();
        for(Object module : m)
            result.add(new ModuleInfo((IModule) module));

        return result;
    }

    @Override
    public void OnError(Exception ex) {
        Helper.Ex2Log(ex);
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

    // region timer state
    private  void initTimerCheckState(){
        timerTask = new CheckStateTimerTask(this);
        timer = new Timer();

        timer.schedule(timerTask, 0, 400);
    }

    private class CheckStateTimerTask extends TimerTask {

        ModuleManager moduleManager;

        public CheckStateTimerTask(ModuleManager m){
            moduleManager = m;
        }

        @Override
        public void run() {
            try {
                moduleManager.checkState();
            }catch (Exception ex)
            {
                Helper.Ex2Log(ex);
            }
        }
    }

    // endregion timer state
}

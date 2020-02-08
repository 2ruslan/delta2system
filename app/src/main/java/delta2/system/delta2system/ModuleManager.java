package delta2.system.delta2system;

import android.content.Context;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import delta2.system.common.Helper;
import delta2.system.common.enums.ModuleState;
import delta2.system.common.interfaces.IError;
import delta2.system.common.interfaces.IInit;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModule;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.common.interfaces.module.IModuleWorker;

public class ModuleManager implements IRequestSendMessage, IReceiveMessage, IInit, IError {

    Context context;

    ModulesList modules;

    CopyOnWriteArrayList<ModuleInfo> transportInfo;
    public CopyOnWriteArrayList<ModuleInfo> GetTransportInfo(){
        return transportInfo;
    }

    CopyOnWriteArrayList<ModuleInfo> workerInfo;
    public CopyOnWriteArrayList<ModuleInfo>  GetWorkerInfo(){
        return workerInfo;
    }

    CopyOnWriteArrayList<ModuleInfo> allModulesInfo;
    public CopyOnWriteArrayList<ModuleInfo> GetAllModulesInfo(){
        return allModulesInfo;
    }

    Timer timer;
    CheckStateTimerTask timerTask;

    IModule AllModules[];


    public ModuleManager(Context c){
        context = c;

        Helper.setWorkDir(context.getFilesDir());

        modules = new ModulesList();

        transportInfo = new CopyOnWriteArrayList<>();
        workerInfo = new CopyOnWriteArrayList<>();
        allModulesInfo = new CopyOnWriteArrayList<>();

        initModules();

        initTimerCheckState();
    }


    // region check modules state
    public void checkState(){
        switch (modules.getModuleState()) {
            case startNeed:
                checkModulesStart();
                break;
            case initNeed:
                checkModulesInit();
                break;
            case none:
                checkModulesWork();
                break;
        }
    }

    private void checkModulesInit(){
        for(IModule m : modules){
            if (m.GetModuleState() == ModuleState.initNeed) {
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
            break;
        }
    }

    private void checkModulesStart(){
        for(IModule m : modules){
            if (m.GetModuleState() == ModuleState.startNeed){
                m.Start();
                break;
            }
        }
    }

    private void checkModulesWork(){


    }
    // endregion check modules state


    private void initModules(){
        // transport
        modules.addModule(new delta2.system.tdropbox.Module(context));
        modules.addModule(new delta2.system.ttelegram.Module(context));

        // worker
        modules.addModule(new delta2.system.whardwareinfo.Module(context));
        modules.addModule(new delta2.system.wmotiondetector.Module(context));

        // set need init
        for(IModule m :modules) {
            if (PreferencesHelper.getIsActiveModule(m.GetModuleID()))
                m.SetStateNeedInit();
        }
    }

    //endregion init work

    @Override
    public void init() {
        initTimerCheckState();
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

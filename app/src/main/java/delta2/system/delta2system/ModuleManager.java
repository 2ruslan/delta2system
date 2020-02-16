package delta2.system.delta2system;

import android.content.Context;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;
import delta2.system.common.commands.Command;
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
import delta2.system.common.messages.MessageText;

public class ModuleManager implements IRequestSendMessage, IReceiveMessage, IInit, IError, INotifyChanged {

    private Context context;
    private INotifyChanged notifyChanged;
    public void SetNotifyChanged(INotifyChanged n){
        notifyChanged = n;
    }

    private void NotifyChanged(){
        if (notifyChanged != null)
            notifyChanged.OnNotifyChanged();
    }


    static ArrayList<IModule> modulesAll;
    static public ArrayList<IModule> GetAllModules(Context c){
        modulesAll = new ArrayList<>();

        // transport
        modulesAll.add(new delta2.system.tdropbox.Module(c));
        modulesAll.add(new delta2.system.ttelegram.Module(c));

        // worker
        modulesAll.add(new delta2.system.whardwareinfo.Module(c));
        modulesAll.add(new delta2.system.wmotiondetector.Module(c));

        return modulesAll;
    }


    ModulesList modules;
    public ModulesList GetModules(){
        return modules;
    }


    Timer timer;
    CheckStateTimerTask timerTask;


    public ModuleManager(Context c){
        context = c;

        Helper.setWorkDir(context.getFilesDir());

        GetAllModules(context);

        modules = new ModulesList();
        modules.SetNotifyChanged(this);
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
                break;
            }
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
     //   OnNotifyChanged();
    }
    // endregion check modules state


    private void initModules(){

        // set need init
        for(IModule m :modulesAll) {
            if (PreferencesHelper.getIsActiveModule(m.GetModuleID())) {
                modules.addModule(m);
                m.SetStateNeedInit();
            }
        }
    }


    @Override
    public void init() {
        initModules();
        initTimerCheckState();
    }

    @Override
    public void destroy() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }


        for(IModule m :modules) {
            m.destroy();
        }
    }

    //region route
    @Override
    public void RequestSendMessage(IMessage msg) {
        MessageExt m = new MessageExt(msg);
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
        if (msg instanceof MessageText) {
            MessageText txtMsg  = (MessageText)msg;

            CommandExt c =  new CommandExt(txtMsg);
            for (IModule module : modules) {
                if (EqualsName(c.module, module)) {
                    if (module instanceof IModuleWorker)
                        ((IModuleWorker) module).ExecuteCommand(c.cmd);
                }
            }
        }
    }

    //endregion route


    // endregion


    private boolean EqualsName(String name,  IModule m){
        return name.equals(MessageExt._ALL) || name.equals(m.GetShortName());
    }

    @Override
    public void OnError(Exception ex) {
        Helper.Ex2Log(ex);
    }

    @Override
    public void OnNotifyChanged() {
        NotifyChanged();
    }

    private class CommandExt{
        public static final String _ALL = "*";

        public CommandExt(MessageText m){
            cmd = new Command(m.GetText());
        }

        public ICommand cmd;
        public String module = _ALL;
    }

    private class MessageExt{
        public static final String _ALL = "*";

        public MessageExt(IMessage m){
            msg = m;
        }

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

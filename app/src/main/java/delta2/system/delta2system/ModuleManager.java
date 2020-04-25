package delta2.system.delta2system;

import android.content.Context;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;
import delta2.system.common.Log.L;
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
import delta2.system.common.messages.MessageCommand;
import delta2.system.common.messages.MessageForward;
import delta2.system.common.messages.MessageText;
import delta2.system.delta2system.commands.ModuleExeCmdManager;


public class ModuleManager implements IRequestSendMessage, IReceiveMessage, IInit, IError, INotifyChanged {

    static private Context context;

    static ArrayList<IModule> modulesAll;

    static ArrayList<String> moduleNames;

    private ModuleExeCmdManager moduleExeCmdManager;

    public ModuleManager(){
        moduleExeCmdManager = new ModuleExeCmdManager(context, this);
    }

    private INotifyChanged notifyChanged;
    public void SetNotifyChanged(INotifyChanged n){
        notifyChanged = n;
    }

    private void NotifyChanged(){
        if (notifyChanged != null)
            notifyChanged.OnNotifyChanged();
    }

    public static void init(Context c){
        context = c;

        modulesAll = new ArrayList<>();

        // transport
        modulesAll.add(new delta2.system.tdropbox.Module(c));
        modulesAll.add(new delta2.system.ttelegram.Module(c));
        modulesAll.add(new delta2.system.ttelephony.Module(c));
        modulesAll.add(new delta2.system.tftp.Module(c));
      //  modulesAll.add(new delta2.system.tline.Module(c));

        // worker
        modulesAll.add(new delta2.system.whardwareinfo.Module(c));
        modulesAll.add(new delta2.system.wmotiondetector.Module(c));
        modulesAll.add(new delta2.system.warduinobridge.Module(c));
        modulesAll.add(new delta2.system.wcaralarm.Module(c));
        modulesAll.add(new delta2.system.wsu.Module(c));
        modulesAll.add(new delta2.system.wtimer.Module(c));
    //    modulesAll.add(new delta2.system.wstartstop.Module(c));

        moduleNames = new ArrayList<>();
        for (IModule m : modulesAll)
            moduleNames.add((m.GetShortName()));
    }

    public static ArrayList<IModule> GetAllModules(){
        return modulesAll;
    }

    static public boolean CheckExistsActiveModule(){
        for (IModule m : modulesAll)
            if (PreferencesHelper.getIsActiveModule(m.GetModuleID()))
                return true;

        return false;
    }

    ModulesList modules;
    public ModulesList GetModules(){
        return modules;
    }


    Timer timer;
    CheckStateTimerTask timerTask;

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
                    if (m instanceof IModuleWorker)
                        ((IModuleWorker)m).RegisterRequestSendMessage(this);
                    else if (m instanceof IModuleTransport)
                        ((IModuleTransport)m).RegisterReceiveMessage(this);

                    m.init();
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


    boolean isStarted = false;
    private void checkModulesWork(){
        if (!isStarted) {
            sendMainInfo();
            isStarted = true;
        }

    }
    // endregion check modules state

    public void sendMainInfo(){
        moduleExeCmdManager.Run(new Command("info"));
    }

    private void initModules(){
        modules = new ModulesList();
        modules.SetNotifyChanged(this);

        // set need init
        for(IModule m :modulesAll) {
            if (PreferencesHelper.getIsActiveModule(m.GetModuleID())) {
                try {
                    modules.addModule(m);
                    m.SetStateNeedInit();
                }
                catch (Exception e){
                    L.log.error("", e);
                }

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
            try {
                m.destroy();
            }
            catch (Exception e){
                L.log.error("destroy error", e);
            }
        }
    }

    //region route
    @Override
    public void RequestSendMessage(IMessage msg) {
        L.log.debug(msg.toString());

        InfoData.PlusSend();

        if (msg instanceof MessageCommand){
            OnReceiveMessage(msg);
        }
        else {
            MessageExt m = new MessageExt(msg);
            for (IModule module : modules) {
                if (EqualsName(m.module, module)) {
                    if (module instanceof IModuleTransport) {
                        try {
                            ((IModuleTransport) module).SendMessage(msg);
                        } catch (Exception e) {
                            L.log.error("", e);
                        }

                    }
                }
            }
        }
    }

    @Override
    public void OnReceiveMessage(IMessage msg) {
        L.log.debug(msg.toString());

        InfoData.PlusReceive();

        if (msg instanceof MessageForward){
            MessageForward f = (MessageForward)msg;
            RequestSendMessage(f.getMessage2forward());
        }

        else if (msg instanceof MessageCommand) {
            runCommand(CommandExt._ALL, ((MessageCommand)msg).getCommand());
        }

        else if (msg instanceof MessageText) {
            MessageText txtMsg  = (MessageText)msg;

            CommandExt c =  new CommandExt(txtMsg);

            if (c.module.equals(CommandExt._ALL))
                runMainCommnds(c.cmd);

            runCommand(c.module, c.cmd);
        }
    }

    private void runCommand(String m, ICommand cmd){

        for (IModule module : modules) {
            if (EqualsName(m, module)) {
                if (module instanceof IModuleWorker)
                    try {
                        ((IModuleWorker) module).ExecuteCommand(cmd);
                    }
                    catch (Exception e){
                        L.log.error("", e);
                    }

            }
        };

    }

    private void runMainCommnds(ICommand command){
        moduleExeCmdManager.Run(command);
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

    private abstract class BaseExt{

        public static final String _ALL = "*";

        public String module = _ALL;
    }

    private class CommandExt extends BaseExt{
        public ICommand cmd;

        public CommandExt(MessageText m){

            String full = m.GetText();
            String text = full;

            if (full.length() > 4 && full.charAt(3) == ' ' ){
                String candidate = full.substring(0, 3);
                if (moduleNames.contains(candidate)){
                    module = candidate;
                    text = full.substring(4);
                }
            }

            cmd = new Command(m, text);
        }
    }

    private class MessageExt extends BaseExt{
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

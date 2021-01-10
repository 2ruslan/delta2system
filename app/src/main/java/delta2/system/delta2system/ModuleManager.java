package delta2.system.delta2system;

import android.content.Context;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Helper;
import delta2.system.framework.base.Bus;
import delta2.system.framework.common.Log;
import delta2.system.framework.interfaces.IBus;
import delta2.system.framework.interfaces.ILogger;
import delta2.system.framework.interfaces.IModule;


public class ModuleManager {

    static private Context context;

    protected ILogger logger = Log.Instance();

    private IBus bus = new Bus();

    private ArrayList<IModule> modulesAll = new ArrayList<>();
    private ArrayList<IModule> activeModules = new ArrayList<>();
    private ArrayList<String> moduleNames = new ArrayList<>();

    private INotifyChanged notifyChanged;
    public void SetNotifyChanged(INotifyChanged n){
        notifyChanged = n;
    }

    private void NotifyChanged(){
        if (notifyChanged != null)
            notifyChanged.OnNotifyChanged();
    }

    public ModuleManager(Context c){
        context = c;

        // transport
     //   modulesAll.add(new delta2.system.tdropbox.Module(c));
        modulesAll.add(new delta2.system.ttelegram.Module(c, bus));
     //   modulesAll.add(new delta2.system.ttelephony.Module(c));
     //   modulesAll.add(new delta2.system.tftp.Module(c));
     //   modulesAll.add(new delta2.system.tmail.Module(c));
      //  modulesAll.add(new delta2.system.tline.Module(c));

        // worker
        modulesAll.add(new delta2.system.whardwareinfo.Module(c, bus));
      //  modulesAll.add(new delta2.system.wmotiondetector.Module(c));
      //  modulesAll.add(new delta2.system.warduinobridge.Module(c));
      //  modulesAll.add(new delta2.system.wcaralarm.Module(c));
      //  modulesAll.add(new delta2.system.wsu.Module(c));
      //  modulesAll.add(new delta2.system.wtimer.Module(c));
    //    modulesAll.add(new delta2.system.wstartstop.Module(c));

        moduleNames = new ArrayList<>();
        for (IModule m : modulesAll) {
            moduleNames.add(m.GetModuleName());
          //  if(PreferencesHelper.getIsActiveModule(m.GetModuleId()))
                activeModules.add(m);
        }
    }

    public ArrayList<IModule> GetAllModules(){
        return modulesAll;
    }

    public boolean CheckExistsActiveModule(){
        return !activeModules.isEmpty();
    }

    public ArrayList<IModule> GetModules(){
        return activeModules;
    }


    Timer timer;
    CheckStateTimerTask timerTask;

    private void initModules() {
        for (IModule m : activeModules) {
            try {
                m.Begin();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public void init() {
        initModules();
        initTimerCheckState();
    }

    public void destroy() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }

        for(IModule m : activeModules) {
            try {
                m.Finish();
            }
            catch (Exception e){
                logger.error(e);
            }
        }
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
              //  moduleManager.checkState();
            }catch (Exception ex)
            {
                Helper.Ex2Log(ex);
            }
        }
    }

    // endregion timer state
}

package delta2.system.delta2system;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import delta2.system.common.Helper;
import delta2.system.common.interfaces.IAcnivityCallback;
import delta2.system.common.interfaces.IInit;
import delta2.system.common.interfaces.commands.ICommand;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.interfaces.messages.IReceiveMessage;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.interfaces.module.IModuleTransport;
import delta2.system.common.interfaces.module.IModuleWorker;
import delta2.system.common.messages.MessageText;


public class ModuleManager implements IRequestSendMessage, IReceiveMessage, IInit, IAcnivityCallback {

    Context context;
    IModuleWorker[] ModuleWorkers;
    IModuleTransport[] ModuleTransports;


    public ModuleManager(Context c){
        context = c;
    }


    public void init()
    {
        Helper.setWorkDir(context.getFilesDir());

        initTransport();
        initWorker();

        checkPermission();

    }

    private void initTransport(){
        ModuleTransports = new IModuleTransport[]
                {
                        new delta2.system.tdropbox.Module(context),
                        new delta2.system.ttelegram.Module(context)
                } ;
    }

    private void initWorker(){
        ModuleWorkers = new IModuleWorker[]
                {
                        new delta2.system.whardwareinfo.Module(context),
                        new delta2.system.wmotiondetector.Module(context)
                } ;
    }

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
        i.putStringArrayListExtra(StarterApp._ALL_PERMISSION, allPermission);
        context.startActivity(i);

    }

    @Override
    public void OnActivityCallback(Intent intent) {
        StarterApp.destroy();

        for( IModuleWorker worker : ModuleWorkers) {
            worker.init();
            worker.RegisterRequestSendMessage(this);
        }

        for(IModuleTransport transport : ModuleTransports) {
            transport.init();
            transport.RegisterReceiveMessage(this);
        }

        //test
        RequestSendMessage(new MessageText("test"));
    }

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

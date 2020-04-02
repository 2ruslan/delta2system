package delta2.system.wtimer.timers;

import java.util.ArrayList;
import java.util.List;

import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wtimer.Preferences.PreferencesHelper;

public class TimerManager {

    List<TimerCommand> commands = new ArrayList<>();

    IRequestSendMessage sender;

    private static TimerManager timerManager;

    public static TimerManager GetInstance(IRequestSendMessage s){
        if (timerManager == null)
            timerManager = new TimerManager(s);

        return timerManager;
    }

    public static TimerManager GetInstance(){
        return timerManager;
    }

    public void destriy(){
        for(TimerCommand c : commands)
            c.cancel();
    }

    private TimerManager(IRequestSendMessage s)
    {
        sender = s;
        Restore();
    }

    public void AddTimer(String rawCommand){
        if (rawCommand == null || rawCommand.trim().equals(""))
            return;

        commands.add( new TimerCommand(sender, rawCommand) );
        Save();
    }

    public void DelTimer(int idx){
        commands.get(idx).cancel();
        commands.remove(idx);
        Save();
    }

    public ArrayList<String> GetRawCommands(){
        ArrayList<String> result = new ArrayList<>();

        for(TimerCommand c : commands)
            result.add(c.GetFullCommand());

        return result;
    }

    private void Save(){
        StringBuilder sb = new StringBuilder();

        for(TimerCommand c : commands)
            sb.append(String.format("%s\n", c.GetFullCommand()));

        PreferencesHelper.setCommands(sb.toString());
    }

    private void Restore(){
        String[] raw = PreferencesHelper.getCommands().split("\n");
        for(String c : raw)
            AddTimer(c);


    }
}



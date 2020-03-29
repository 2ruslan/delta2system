package delta2.system.wtimer.at;

public abstract class AtBaseCommand {
    EnTimerType timerType;
    String command;

    public AtBaseCommand(EnTimerType t, String c){
        timerType = t;
        command = c;
    }
}

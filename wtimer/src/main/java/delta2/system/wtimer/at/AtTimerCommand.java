package delta2.system.wtimer.at;

public class AtTimerCommand extends AtBaseCommand {

    int period;

    public AtTimerCommand( String c, int p) {
        super(EnTimerType.timer, c);

        period = p;
    }
}

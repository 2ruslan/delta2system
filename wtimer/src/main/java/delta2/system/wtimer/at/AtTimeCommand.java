package delta2.system.wtimer.at;

import java.sql.Time;

public class AtTimeCommand extends AtBaseCommand {

    Time time;

    public AtTimeCommand(String c, Time t) {
        super(EnTimerType.time, c);

        time = t;
    }
}

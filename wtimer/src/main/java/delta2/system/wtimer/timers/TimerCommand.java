package delta2.system.wtimer.timers;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageCommand;

public class TimerCommand extends Timer {

    public TimerCommand(IRequestSendMessage sender, String command, Date dt, long period){
        scheduleAtFixedRate(new AtTimerTask(sender, command), dt, period);
    }

    private class AtTimerTask  extends TimerTask {

        private IRequestSendMessage requestSendMessage;
        private MessageCommand message;
        public AtTimerTask(IRequestSendMessage s, String c) {
            requestSendMessage = s;
            message = new MessageCommand( new Command("", c));
        }

        @Override
        public void run() {
            requestSendMessage.RequestSendMessage(message);
        }
    }
}

package delta2.system.wtimer.timers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import delta2.system.common.Log.L;
import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageCommand;
import delta2.system.common.messages.MessageText;

public class TimerCommand extends Timer {

    public static final String _TIME = "-t";
    public static final String _DATE = "-d";
    public static final String _PERIOD = "-p";

    private String fullCommand;
    public String GetFullCommand(){
        return fullCommand;
    }


    public TimerCommand(IRequestSendMessage sender, String rawCommand){
        fullCommand = rawCommand;

        long period = -1;
        Date dtD = null;
        Date dtT = null;
        String cmd = null;

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

        try {
            for (TokenParser.TokenResult t : TokenParser.Parse(rawCommand, new String[]{_TIME, _DATE, _PERIOD})) {
                if (t.GetKey().equals(_PERIOD))
                    period = GetPeriod(t.GetValue());
                else if (t.GetKey().equals(_DATE)) {
                    dtD = formatDate.parse(t.GetValue());
                }
                else if (t.GetKey().equals(_TIME)) {
                    dtT = formatTime.parse(t.GetValue());
                } else if (t.GetKey().equals(TokenParser.TokenResult._COMMAND)){
                    cmd = t.GetValue();
                }
            }

            if (period != -1 && cmd != null){
                Date dt =  dtD != null ? dtD : Calendar.getInstance().getTime();
                if (dtT != null)
                    dt = new Date( GetDateOnly(dt).getTime() + dtT.getTime() );

                scheduleAtFixedRate(new AtTimerTask(sender, cmd), dt, period);
            }

        }catch (Exception ex){
            sender.RequestSendMessage(new MessageText(ex.getMessage()));
            L.log.error("", ex);
        }

    }

    private Date GetDateOnly(Date d){
        long millisInDay = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / millisInDay) * millisInDay;
        return new Date(dateOnly);
    }

    private long GetPeriod(String val){
        long result = -1;

        String[] parts = val.split(":");

        if (parts.length > 0){
            int m = Integer.valueOf(parts[parts.length - 1]);
            result = m * 60 * 1000;
        }

        if (parts.length > 1){
            int h = Integer.valueOf(parts[parts.length - 2]);
            result += h * 60 * 60 * 1000;
        }

        if (parts.length > 2){
            int d = Integer.valueOf(parts[parts.length - 3]);
            result += d * 24 * 60 * 60 * 1000;
        }

        return result;
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

    public static class TokenParser{
        public static List<TokenResult> Parse(String raw, String[] tokens){

            ArrayList<TokenResult> result = new ArrayList<>();

            for(int i = 0; i < tokens.length; i++){
                String token = tokens[i];
                int spos = raw.indexOf(token);
                if (spos >= 0){
                    int vspos = spos + token.length();
                    int epos = raw.indexOf(" ", vspos + 1);
                    if (epos > spos) {
                        String val = raw.substring(vspos, epos).trim();
                        result.add( new TokenResult(token, val) );

                        raw = raw.substring(0, spos) + raw.substring(epos).trim();
                    }
                }
            }

            result.add( new TokenResult(TokenResult._COMMAND, raw.trim()) );

            return result;
        }

        public static class TokenResult{
            public static final String _COMMAND = "_COMMAND";

            private String key;
            public  String GetKey(){
                return key;
            }
            private String value;
            public String GetValue(){
                return value;
            }

            public TokenResult(String k,String v){
                key = k;
                value = v;
            }

        }
    }
}

package delta2.system.common.execmd;

public class ExeCmdResult {
    public static ExeCmdResult None = new ExeCmdResult(enState.none, false);

    public enum enState{
        none,
        error,
        ok
    }

    private enState state;
    private String msg;
    private boolean needAnswer;

    public ExeCmdResult(enState s, boolean n){
        state = s;
        needAnswer = n;
    }

    public ExeCmdResult(enState s, String m, boolean n){
        state = s;
        msg = m;
        needAnswer = n;
    }

    public enState GetState(){
        return state;
    }

    public String GetMessage(){
        return msg;
    }

    public boolean IsNeedAnswer(){
        return needAnswer;
    }

    public boolean IsMessageNotEmpty(){
        return msg != null && msg.length() > 0;
    }
}


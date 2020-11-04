package delta2.system.wsu.commands;

import android.content.Context;

import java.io.InputStreamReader;
import java.io.Reader;

import delta2.system.common.Log.L;
import delta2.system.common.execmd.ExeBaseCmd;
import delta2.system.common.execmd.ICmdParams;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.wsu.R;

public class CmdTop extends ExeBaseCmd {

    public CmdTop(Context c, IRequestSendMessage s) {
        super(c, s);
    }

    @Override
    protected String GetCommandText() {
        return "top";
    }

    @Override
    protected boolean IsNeedAnswer() {
        return true;
    }

    @Override
    protected String RunCommand(ICmdParams params, String msgId) {
        return top();
    }

    @Override
    protected ICmdParams ParseParams(String args) {
        return EmptyCmdParams;
    }

    @Override
    public String GetHelp() {
        return String.format("%s - %s", GetCommandText(), context.getString(R.string.wsu_top));
    }

    private String top() {
        StringBuilder sb = new StringBuilder();
        try {
            String[] cmd = {"sh","-c",
                    "top -m 100 -n 1 | head -n 17"
            };
            Process process = Runtime.getRuntime().exec(cmd);
            Reader reader = new InputStreamReader(process.getInputStream());

            for (int chr; (chr = reader.read()) != -1; ) {
                sb.append((char) chr);
            }

            process.waitFor();
        } catch (Exception ex) {
            L.log.error("Could not top", ex);
        }
        return sb.toString();
    }


}

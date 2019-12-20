package delta2.system.wmotiondetector.motiondetector;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import delta2.system.common.Helper;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdAutostartGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraAngleGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraAngleSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSizeGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSizeSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdDeltaGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdDeltaSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdHelp;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdInfo;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdPhotoGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdStart;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdStop;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdTurn;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdVoiceCallGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdVoiceCallSet;
import delta2.system.wmotiondetector.motiondetector.Common.CmdBase;
import delta2.system.wmotiondetector.motiondetector.Common.ResultCmd;
import delta2.system.wmotiondetector.motiondetector.Mediator.ICommandCheckMessage;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;


public class CommandManager implements ICommandCheckMessage {
    private static final String _OK = "ok";
    private static final String _NO = "???";

    private List<CmdBase> _commands;

    private CmdBase _CmdPhotoGet;

    private Context _context;

    public CommandManager(Context context){
        _context = context;


        _commands = new ArrayList<>();
        _CmdPhotoGet = new CmdPhotoGet();

        _commands.add(new CmdStart());
        _commands.add(new CmdStop());
        _commands.add(new CmdTurn());
        
        _commands.add(new CmdCameraSet());
        _commands.add(new CmdCameraGet());

        _commands.add(new CmdCameraSizeSet());
        _commands.add(new CmdCameraSizeGet());

        _commands.add(new CmdCameraAngleSet());
        _commands.add(new CmdCameraAngleGet());

        _commands.add(new CmdDeltaSet());
        _commands.add(new CmdDeltaGet());

        _commands.add(new CmdAutostartGet());

        _commands.add(new CmdVoiceCallSet());
        _commands.add(new CmdVoiceCallGet());

        _commands.add(new CmdInfo());
        _commands.add(new CmdHelp());

    }

    public void onDestroy(){

    }


    public void CheckMessage(String inTxt) {

        Helper.Log("CheckMessage", inTxt);

        inTxt = inTxt.toLowerCase();

        if(inTxt.contains(", ")){
            if(!inTxt.startsWith(PreferencesHelper.getDeviceName()))
                return;

            inTxt = inTxt.replace(PreferencesHelper.getDeviceName(), "");
            inTxt = inTxt.replace(", ", "");

        }



            CheckMessage(inTxt, false, false) ;

    }

    public void CheckMessage(String inTxt, boolean repeatCmd, boolean isSilent) {

        inTxt = inTxt.toLowerCase();
        String[] inLines = inTxt.split("\\s+");

        if (inLines.length > 0) {

            String first = inLines[0];
            CmdBase.en_type cmdType = first.equals("get") ? CmdBase.en_type.get :
                    first.equals("set") ? CmdBase.en_type.set :
                            CmdBase.en_type.other;

            ResultCmd cmdResult = null;

            for (CmdBase cmd : _commands) {
                if (cmd.type == cmdType && (
                        (inLines.length > 1 && cmd.cmd.equals(inLines[1]))
                                || (inLines.length == 1 && cmd.cmd.equals(first)))
                        ) {
                    cmdResult = cmd.exec(_context, inTxt, inLines);
                    break;
                }
            }

            if (cmdResult == null) {
                if (cmdType == CmdBase.en_type.get || cmdType == CmdBase.en_type.set) {
                    if (!isSilent)
                        MediatorMD.sendText(repeatCmd ? (inTxt + " : ") : "" + _NO);
                } else {
                    chkError(_CmdPhotoGet.exec(_context, inTxt, inLines));
                }
            } else if (cmdResult.result == CmdBase.en_result.ok &&
                    (cmdType == CmdBase.en_type.set
                            || CmdStart._COMMAND.equals(inLines[0])
                            || CmdStop._COMMAND.equals(inLines[0]))
                    ) {
                if (!isSilent)
                    MediatorMD.sendText((repeatCmd ? (inTxt + " : ") : "") + _OK);
                else
                    chkError(cmdResult);
            }
        }
    }

    private void chkError(ResultCmd cmdResult){
        if (cmdResult.result == CmdBase.en_result.error_msg)
            MediatorMD.sendText(cmdResult.msg);
        else if (cmdResult.result == CmdBase.en_result.exception)
            MediatorMD.sendText("error : " + cmdResult.msg);
    }
}

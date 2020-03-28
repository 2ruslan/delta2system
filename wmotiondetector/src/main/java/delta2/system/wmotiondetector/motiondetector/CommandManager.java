package delta2.system.wmotiondetector.motiondetector;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import delta2.system.common.Helper;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraAngleGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraAngleSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSizeGet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSizeSet;


import delta2.system.wmotiondetector.motiondetector.Commands.CmdPhotoGet;

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

        _commands.add(new CmdCameraSet());
        _commands.add(new CmdCameraGet());

        _commands.add(new CmdCameraSizeSet());
        _commands.add(new CmdCameraSizeGet());

        _commands.add(new CmdCameraAngleSet());
        _commands.add(new CmdCameraAngleGet());


        _commands.add(new CmdVoiceCallSet());
        _commands.add(new CmdVoiceCallGet());


    }

    public void onDestroy(){

    }


    public void CheckMessage(String msgId, String inTxt) {

        Helper.Log("CheckMessage", inTxt);

        inTxt = inTxt.toLowerCase();

        if(inTxt.contains(", ")){
            if(!inTxt.startsWith(PreferencesHelper.getDeviceName()))
                return;

            inTxt = inTxt.replace(PreferencesHelper.getDeviceName(), "");
            inTxt = inTxt.replace(", ", "");

        }

         CheckMessage(msgId, inTxt, true, false) ;

    }

    public void CheckMessage(String msgId, String inTxt, boolean repeatCmd, boolean isSilent) {

        inTxt = inTxt.toLowerCase();
        String[] inLines = inTxt.split("\\s+");

        if (inLines.length > 0) {

            String first = inLines[0];
            CmdBase.en_type cmdType = first.equals("get") ? CmdBase.en_type.get :
                    first.equals("set") ? CmdBase.en_type.set :
                            CmdBase.en_type.other;

            ResultCmd cmdResult = null;

            boolean isCmdExists = false;
            for (CmdBase cmd : _commands) {
                if (cmd.type == cmdType && (
                        (inLines.length > 1 && cmd.cmd.equals(inLines[1]))
                                || (inLines.length == 1 && cmd.cmd.equals(first)))
                        ) {
                    cmdResult = cmd.exec(msgId,_context, inTxt, inLines);
                    isCmdExists = true;
                    break;
                }
            }

            if (!isCmdExists )
                cmdResult =  _CmdPhotoGet.exec(msgId,_context, inTxt, inLines);

            if (cmdResult == null) {
                if (cmdType == CmdBase.en_type.get || cmdType == CmdBase.en_type.set) {
                    if (!isSilent)
                        MediatorMD.sendText(msgId, repeatCmd ? (inTxt + " : ") : "" + _NO);
                } else {
                    chkError(_CmdPhotoGet.exec(msgId,_context, inTxt, inLines));
                }
            } else if (cmdResult.result == CmdBase.en_result.ok && cmdType == CmdBase.en_type.set) {
                if (!isSilent)
                    MediatorMD.sendText(msgId,(repeatCmd ? (inTxt + " : ") : "") + _OK);
                else
                    chkError(cmdResult);
            }
        }
    }

    private void chkError(ResultCmd cmdResult){
        if (cmdResult.result == CmdBase.en_result.error_msg)
            MediatorMD.sendText(IMessage._NO_MSG_ID, cmdResult.msg);
        else if (cmdResult.result == CmdBase.en_result.exception)
            MediatorMD.sendText(IMessage._NO_MSG_ID,"error : " + cmdResult.msg);
    }
}

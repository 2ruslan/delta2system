package delta2.system.wmotiondetector.motiondetector;

import android.content.Context;

import delta2.system.wmotiondetector.R;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraAngleSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSet;
import delta2.system.wmotiondetector.motiondetector.Commands.CmdCameraSizeSet;
import delta2.system.wmotiondetector.motiondetector.Detector.CamearaProps;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class InfoHelper {
    public static void sendInfo( String msgId, Context context){
        StringBuilder sb = new StringBuilder("MotionDetector");
        sb.append("\n\n------------------");

        sb.append( String.format("\n\n%s = %s", context.getResources().getString(R.string.wmd_status), context.getResources().getString( PreferencesHelper.GetIsActive() ? R.string.wmd_status_started : R.string.wmd_status_stopped)));

        sb.append(String.format("\n\n%s = %s",context.getResources().getString(R.string.wmd_delta), PreferencesHelper.getDelta()));

        sb.append(getCamProp(CmdCameraSet._COMMAND));
        sb.append(getCamProp(CmdCameraSizeSet._COMMAND));
        sb.append(getCamProp(CmdCameraAngleSet._COMMAND));


        MediatorMD.sendText (msgId, sb.toString());
    }

    private static String getCamProp(String prop){
        CamearaProps p = MediatorMD.GetCameraProps(prop);
        return String.format("\n%s : %s", prop, p.values.get(p.current));
    }

}

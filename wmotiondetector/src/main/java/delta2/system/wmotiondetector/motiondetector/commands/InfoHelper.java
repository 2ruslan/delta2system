package delta2.system.wmotiondetector.motiondetector.commands;

import android.content.Context;

import delta2.system.common.Helper;
import delta2.system.wmotiondetector.Module;
import delta2.system.wmotiondetector.R;

import delta2.system.wmotiondetector.motiondetector.Detector.CamearaProps;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;
import delta2.system.wmotiondetector.motiondetector.Preferences.PreferencesHelper;

public class InfoHelper {
    public static String GetInfo( Context context){
        StringBuilder sb = new StringBuilder(Helper.GetMessageHeader(context.getString(R.string.wmd_module_name), Module._MODULE_CODE));

        sb.append( String.format("\n\n%s = %s", context.getResources().getString(R.string.wmd_status), context.getResources().getString( PreferencesHelper.GetIsActive() ? R.string.wmd_status_started : R.string.wmd_status_stopped)));

        sb.append(String.format("\n\n%s = %s",context.getResources().getString(R.string.wmd_delta), PreferencesHelper.getDelta()));

        sb.append(getCamProp(CmdCameraGet._COMMAND));
        sb.append(getCamProp(CmdCameraSizeGet._COMMAND));
        sb.append(getCamProp(CmdCameraAngleGet._COMMAND));

        sb.append(String.format("\n\n%s", context.getString(R.string.delimiter_line)));

        return sb.toString();
    }

    private static String getCamProp(String prop){
        CamearaProps p = MediatorMD.GetCameraProps(prop);
        return String.format("\n%s : %s", prop, p.values.get(p.current));
    }

}

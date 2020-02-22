package delta2.system.ttelephony.transporttelephony.Transport;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

import delta2.system.common.Log.L;
import delta2.system.ttelephony.Module;
import delta2.system.ttelephony.transporttelephony.Preferences.PreferencesHelper;

public class CallReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
            String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = "";
            if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                try {
                    number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    String endNumber = number.substring(number.length() - 6);

                    String etPh = PreferencesHelper.getPhoneNum();
                    etPh = etPh.substring(etPh.length() - 6);

                    if (endNumber.equals(etPh)) {
                        Module.OnRecieveMsg("turn");
                    }
                    //else{
                    //    MediatorMD.TexSendText("call from " + number, null);
                    //}

                }
                catch (Exception e){
                    //if (!number.equals(""))
                    //    MediatorMD.TexSendText("call from " + number, null);
                    //Helper.Ex2Log(e);
                    L.log.error("CallReceiver", e);
                }
                finally {
                    endCall(context);
                }

            }
        }
    }

    public static void endCall(Context context){
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
            methodGetITelephony.setAccessible(true);
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
            Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            methodEndCall.invoke(telephonyInterface);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
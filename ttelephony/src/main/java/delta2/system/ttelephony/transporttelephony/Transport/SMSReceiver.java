package delta2.system.ttelephony.transporttelephony.Transport;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsMessage;

import delta2.system.common.Log.L;
import delta2.system.ttelephony.Module;
import delta2.system.ttelephony.transporttelephony.Preferences.PreferencesHelper;


public class SMSReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            try {
                Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
                SmsMessage[] messages = new SmsMessage[pduArray.length];
                for (int i = 0; i < pduArray.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                }

                String sms_from_full = messages[0].getDisplayOriginatingAddress();

                String sms_from = sms_from_full;
                String etPh = PreferencesHelper.getPhoneNum();

                sms_from = sms_from.substring(sms_from.length() - 6);
                etPh = etPh.substring(etPh.length() - 6);

                StringBuilder bodyText = new StringBuilder();
                for (int i = 0; i < messages.length; i++) {
                    bodyText.append(messages[i].getMessageBody());
                }
                String body = bodyText.toString();

                if (sms_from.equals(etPh))
                    Module.OnRecieveMsg(body);
                //else
                //    MediatorMD.TexSendText(sms_from_full + " : " + body, null);

                /**/
                ContentValues values = new ContentValues();
                values.put("read",true);
                context.getContentResolver().update(Uri.parse("content://sms/inbox"),values,
                        "_id="+messages[0].getIndexOnIcc() , null);

            } catch (Exception ex) {
                L.log.error("onReceive", ex);
            }
        }
    }
}

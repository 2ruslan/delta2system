package delta2.system.ttelephony.transporttelephony.Transport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Calendar;

import delta2.system.common.Log.L;
import delta2.system.common.interfaces.messages.IMessage;
import delta2.system.common.messages.MessageText;
import delta2.system.common.messages.MessageVoiceCall;
import delta2.system.ttelephony.Module;
import delta2.system.ttelephony.transporttelephony.Preferences.PreferencesHelper;

public class TelephonyTransport  {

    private Context conrtext;

    public TelephonyTransport(Context c){
        conrtext = c;
    }

    public void SendMessage(IMessage msg) {
        if (PreferencesHelper.getPhoneNum() == null || PreferencesHelper.getPhoneNum().length() < 5 )
            return;

        if (msg instanceof MessageText)
        {
            if (PreferencesHelper.getSendText() || msg.getSrcModule().equals(Module._MODULE_NAME) ) {
                MessageText m = (MessageText) msg;
                sendTxt(m.GetText());
            }
        }
        if (msg instanceof MessageVoiceCall && PreferencesHelper.getVoiceCall()){
            callVoice();
        }
    }

    public void sendTxt(String msg) {
        if(!PreferencesHelper.getSendText())
            return;

        sendSMS(PreferencesHelper.getPhoneNum(), msg);
    }


    public void callVoice() {
        call2Phone(conrtext);
    }


    static long _lastCall = 0;
    @SuppressLint("MissingPermission")
    public static void call2Phone(Context c){

        if(!PreferencesHelper.getVoiceCall())
            return;

        String phone = PreferencesHelper.getPhoneNum();

        if (phone.equals(""))
            return;

        long now = Calendar.getInstance().getTimeInMillis();

        if(now - _lastCall  < 300000) // 5 min
            return;

        _lastCall = now;

        Intent intent = new Intent(Intent.ACTION_CALL
                , Uri.parse("tel: +" + phone.replace("+", "") ));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


        c.startActivity(intent);
    }


    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(conrtext, 0,
                new Intent(conrtext, SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(conrtext, 0,
                new Intent(conrtext, SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {
            L.log.error("call2Phone",e);
        }

    }

    public class SmsDeliveredReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:

                    break;
                case Activity.RESULT_CANCELED:

                    break;
            }
        }
    }

    public class SmsSentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:


                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:


                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:


                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:

                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:

                    break;
            }
        }
    }
}

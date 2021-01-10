package delta2.system.whardwareinfo.hardwareinfo.hardware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;

import java.util.Calendar;

import delta2.system.framework.common.Log;
import delta2.system.framework.common.MessageFactory;
import delta2.system.framework.interfaces.ILogger;
import delta2.system.framework.interfaces.IMessageReceiver;
import delta2.system.whardwareinfo.R;
import delta2.system.whardwareinfo.hardwareinfo.preferences.PreferencesHelper;

public class BatteryLevelReceiver extends BroadcastReceiver {

    static final int _NO_VALUE = -100;

    static final int _MIN_INTERVAL = 3 * 60 * 1000;

    static Context mContext;

    static float mLevel = _NO_VALUE;

    static float mTemp = _NO_VALUE;

    static int mBatStatus = _NO_VALUE;

    static float mVoltage = _NO_VALUE;

    static int mHealth = _NO_VALUE;

    static long _lastPowerMinPrw = _MIN_INTERVAL;

    static float lastLevel = 100;

    private static IMessageReceiver receiver;
    private static ILogger logger = Log.Instance();

    public static void init(Context context, IMessageReceiver r){
        mContext = context;
        receiver = r;
    }

    public  static  void destroy(){
        mContext = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        String action = intent.getAction();

        int val = intent.getIntExtra(BatteryManager.EXTRA_STATUS, _NO_VALUE);
        if(val != _NO_VALUE)
            mBatStatus = val;

        float valf = ((float) (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, _NO_VALUE)));

        if(valf != _NO_VALUE)
            mTemp = valf / 10f;

        val = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, _NO_VALUE);

        boolean levelChange = val != mLevel;

        if(val != _NO_VALUE)
            mLevel = val;

        val = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,_NO_VALUE);

        if(val != _NO_VALUE)
            mVoltage = val;
         if(mVoltage > 1000)
                mVoltage /=1000f;

        val = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, _NO_VALUE);
        if(val != _NO_VALUE)
            mHealth = val;


        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
            mBatStatus = BatteryManager.BATTERY_STATUS_CHARGING;

            if(PreferencesHelper.getNotifyPower())
                sendPowerState();

        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            mBatStatus = BatteryManager.BATTERY_STATUS_NOT_CHARGING;
            if(PreferencesHelper.getNotifyPower())
                sendPowerState();
        }
        if(action.equals(Intent.ACTION_BATTERY_LOW)) {
            if( Calendar.getInstance().getTimeInMillis() - _lastPowerMinPrw > _MIN_INTERVAL
                   && levelChange && PreferencesHelper.getNotifyPower())
                sendPowerMinLevel();

            _lastPowerMinPrw = Calendar.getInstance().getTimeInMillis();
        }

        if(mLevel != _NO_VALUE &&
                mLevel >= 0 && mLevel%10 == 0
                    && mLevel <= 15//PreferencesHelper.GetPwrBatMin()
                    && lastLevel > mLevel) {
            sendPowerProc(mLevel);
            lastLevel = mLevel;
        }

    }

    private void sendPowerMinLevel(){
        String msg = mContext.getString(R.string.whi_msg_bat_low);
        receiver.OnReceiveMessage(MessageFactory.GetMessageSendText(msg));
    }

    private static void sendPowerState(){
        receiver.OnReceiveMessage(MessageFactory.GetMessageSendText(getBatInfo()));
    }

    private void sendPowerProc(float level){
        String msg = mContext.getString(R.string.whi_msg_bat_levelprc) + " = " +  level;
        receiver.OnReceiveMessage(MessageFactory.GetMessageSendText(msg));
    }

    public static String getBatInfo(){
        if (mContext == null)
            return "";

        String startStr = "\n  ";

        StringBuilder sb = new StringBuilder();

        sb.append( mContext.getString(R.string.whi_msg_battery));

        if(mBatStatus == BatteryManager.BATTERY_STATUS_FULL)
            sb.append(startStr + mContext.getString(R.string.whi_msg_power_full));
        else if (mBatStatus == BatteryManager.BATTERY_STATUS_CHARGING)
            sb.append(startStr + mContext.getString(R.string.whi_msg_power_on));
        else if (mBatStatus == BatteryManager.BATTERY_STATUS_DISCHARGING || mBatStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING)
            sb.append(startStr + mContext.getString(R.string.whi_msg_power_off));

        if(mHealth != _NO_VALUE)
            sb.append(startStr + mContext.getString(R.string.whi_msg_bat_haelth) + " : " + getHelathStr(mHealth));

        if(mTemp != _NO_VALUE)
            sb.append(startStr + mContext.getString(R.string.whi_msg_bat_temperature) + " : " + mTemp);

        if(mLevel != _NO_VALUE)
            sb.append(startStr + mContext.getString(R.string.whi_msg_bat_level) + " : " + mLevel);

        if(mVoltage != _NO_VALUE)
            sb.append(startStr + mContext.getString(R.string.whi_msg_bat_voltage) + " : " + mVoltage + " V");

        try {
            String cc = getBatteryCapacity(mContext);
            if (!cc.equals(""))
                sb.append(startStr + cc);
        }catch (Exception e)
        {
            logger.error(e);
        }

        return sb.toString();

    }


    static String getHelathStr(int h){
        String result = "";
        if(h == BatteryManager.BATTERY_HEALTH_COLD)
            result = "COLD";
        else if(h == BatteryManager.BATTERY_HEALTH_DEAD)
            result = "DEAD";
        else if(h == BatteryManager.BATTERY_HEALTH_GOOD)
            result = "GOOD";
        else if(h == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE)
            result = "OVER VOLTAGE";
        else if(h == BatteryManager.BATTERY_HEALTH_OVERHEAT)
            result = "OVERHEAT";
        else if(h == BatteryManager.BATTERY_HEALTH_UNKNOWN)
            result = "UNKNOWN";
        else if(h == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE)
            result = "UNSPECIFIED FAILURE";

        return result;
    }

    public static String getBatteryCapacity(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null && chargeCounter != Long.MIN_VALUE ) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);

                return chargeCounter + " mah / " + value + " mah (" + capacity + "%)";
            }
        }

        return "";
    }

}

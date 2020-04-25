package delta2.system.whardwareinfo.hardwareinfo.Hardware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import delta2.system.common.Helper;
import delta2.system.common.Log.L;
import delta2.system.common.messages.MessageText;
import delta2.system.whardwareinfo.Module;
import delta2.system.whardwareinfo.R;
import delta2.system.whardwareinfo.hardwareinfo.Mediator.MediatorMD;
import delta2.system.whardwareinfo.hardwareinfo.Preferences.PreferencesHelper;

public class WifiReceiver extends BroadcastReceiver {

    static Context mContext;
    static int prevType = ConnectivityManager.TYPE_DUMMY;

    static long startWIFI = 0;
    static long startMOBILE = 0;


    public static void init(Context context){
        try {
            mContext = context;
            long total = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
            startMOBILE = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
            startWIFI = (total - startMOBILE);
        }catch (Exception e){
            Helper.Ex2Log(e);
        }

    }

    public static void destroy(){
        mContext = null;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!PreferencesHelper.getNotifyConnection())
            return;

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan != null) {
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null) {
                sendPowerState(netInfo.getType());
            }
        }
    }


    private void sendPowerState(int type){
        if(prevType != type) {
            if (prevType != ConnectivityManager.TYPE_DUMMY) {
                String info = "none";
                try{
                    info = getConInfo(type);
                }
                catch (Exception e){
                    L.log.error("wifi error", e);
                }

                String msg = mContext.getString(R.string.whi_msg_connection_changed) + "\n" + info;
                MediatorMD.RequestSendMessage(new MessageText(Module._MODULE_CODE, msg));
            }
            prevType =type;
        }
    }

    public static String getConInfo(){
        if(mContext == null)
            return "";

        ConnectivityManager conMan = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo!= null)
            return  getConInfo(netInfo.getType());
        else
            return "";
    }

    public static String getConInfo(int type){
        if (mContext == null)
            return  "";

        String tp = "???";
        if (type == ConnectivityManager.TYPE_WIFI)
            tp = "wifi";
        else if (type == ConnectivityManager.TYPE_BLUETOOTH)
            tp = "bluetooth";
        else if (type == ConnectivityManager.TYPE_MOBILE)
            tp = "mobile";
        else if (type == ConnectivityManager.TYPE_ETHERNET)
            tp = "ethernet";
        else if (type == ConnectivityManager.TYPE_VPN)
            tp = "vpn";


        long total = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        long mobileTotal =  TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
        long wifiTotal = total - mobileTotal;

        mobileTotal -= startMOBILE;
        wifiTotal -= startWIFI;

        return mContext.getString(R.string.whi_msg_connection_current) + " : " + tp +
                (wifiTotal > 0 ? "\r\n  wifi : " + bytesToSize(wifiTotal) : "") +
                (mobileTotal > 0 ? "\r\n  mobile : " + bytesToSize(mobileTotal) : "")
                ;
    }

    public static String bytesToSize(long bytes) {
        StringBuilder sb = new StringBuilder();

        if (bytes>=1073741824){
            long gb = bytes/1073741824;
            sb.append(gb + " Gb; ");
            bytes -= (gb*1073741824);
        }

        if (bytes>=1048576){
            long mb = bytes/1048576;
            sb.append(mb + " Mb; ");
            bytes -= (mb*1048576);
        }

        if (bytes>=1024){
            long kb = bytes/1024;
            sb.append(kb + " Kb; ");
            bytes -= (kb*1024);
        }

        sb.append(bytes + " bytes");

        return sb.toString();

    }

    private boolean isNetworkConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (Exception e) { return false; }
    }

}

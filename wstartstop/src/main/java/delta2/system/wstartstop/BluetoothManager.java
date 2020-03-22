package delta2.system.wstartstop;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import delta2.system.common.Log.L;
import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageCommand;
import delta2.system.common.messages.MessageText;
import delta2.system.wstartstop.Preferences.PreferencesHelper;


public class BluetoothManager {
    private Context _context;
    private BluetoothAdapter mAdapter;
    BluetoothDevice device;
    IRequestSendMessage requestSendMessage;

    int prevState = -1;

    public BluetoothManager(Context c){
        _context = c;

        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void Start(){
        initTimertask();
    }

    public void setRequestSendMessage(IRequestSendMessage m){
        requestSendMessage = m;
    }

    public void chekConnection(){

        if (PreferencesHelper.getDeviceAddress().equals(""))
            return;

        if (device == null || !device.getAddress().equals(PreferencesHelper.getDeviceAddress()))
            device = mAdapter.getRemoteDevice(PreferencesHelper.getDeviceAddress());


        int state = device.getBondState();

        if (prevState != state){
            requestSendMessage.RequestSendMessage( new MessageCommand(
                    new Command("", state == BluetoothDevice.BOND_BONDED ? "stop" : "start" )
            ) );
        }
        prevState = state;
    }


    OnlineTimertask _TimerTask ;
    Timer _Timer;

    private  void initTimertask(){
        _TimerTask = new OnlineTimertask(this);
        _Timer = new Timer();

        _Timer.schedule(_TimerTask, 0,  10 *1000);
    }

    //----------------------------------------------------------------

    private class OnlineTimertask extends TimerTask {

        BluetoothManager _manager;
        public OnlineTimertask(BluetoothManager m){
            _manager = m;
        }

        @Override
        public void run() {
            try {
                chekConnection();
            }catch (Exception ex)
            {
                L.log.error("", ex);
            }
        }
    }

}

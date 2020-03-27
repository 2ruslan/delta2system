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
    private BluetoothSocket mBluetoothSocket;
    private Handler bluetoothIn;
    final int handlerState = 0;
    private ConnectedThread mConnectedThread;
    private IRequestSendMessage requestSendMessage;

    private boolean needReconect = false;

    public BluetoothManager(Context c){
        _context = c;

        bluetoothIn = new Handler() {
            StringBuilder sb = new StringBuilder();

            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;

                    sb.append(readMessage);
                    if(sb.toString().contains("\r")) {
                        String str = sb.toString();
                        String[] parts = str.split("\r");
                        sb = new StringBuilder();
                        if (parts.length > 1){
                            for (int i = 1; i < parts.length; i++)
                                sb.append(parts[i]);
                        }
                    }
                }
            }

        };

        mAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    public void Start(){
        initTimertask();
    }

    public void setRequestSendMessage(IRequestSendMessage m){
        requestSendMessage = m;
    }


    boolean prevIsConnected = false;
    private void requestSendMessage(boolean isConnected) {

        if (prevIsConnected == isConnected)
            return;

        if (requestSendMessage != null)
                requestSendMessage.RequestSendMessage(new MessageCommand(new Command("", isConnected ? "stop" : "start" )));

    }


    public void connect(){
        if(!PreferencesHelper.getDeviceAddress().equals("") ){
            L.log.debug("bluetoth connect start");
            if (mConnectedThread != null){
                try {
                    mConnectedThread = null;
                }
                catch (Exception e){
                    L.log.error("bluetoth disconnect 0", e);
                }
            }

            if (mBluetoothSocket != null){
                try {
                    mBluetoothSocket.close();
                    mBluetoothSocket = null;
                }
                catch (Exception e){
                    L.log.error("bluetoth disconnect 1", e);
                }
            }

            try {
                BluetoothDevice device = mAdapter.getRemoteDevice(PreferencesHelper.getDeviceAddress());

                mBluetoothSocket = device.createRfcommSocketToServiceRecord( Common._BLUETOOTH_UID);

                mBluetoothSocket.connect();

                requestSendMessage(true);

                mConnectedThread = new ConnectedThread(mBluetoothSocket);
                mConnectedThread.start();

                needReconect = false;

            }catch (Exception e){
                requestSendMessage(false);
                needReconect = true;
            }
            L.log.debug("bluetoth connect end");
        }
    }


    public void BTSendText(String txt) {
        if (txt.length() > 0 && mConnectedThread != null) {
            mConnectedThread.write(txt);
        }
    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    L.log.error("bluetoothIn read ", e);
                    needReconect = true;
                    break;
                }
            }
        }

        public void write(String input) {
            byte[] msgBuffer = input.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                L.log.error("", e);
                needReconect = true;
            }
        }
    }

    private void chekConnection(){

        if (mBluetoothSocket == null || !mBluetoothSocket.isConnected() || needReconect)
            connect();
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

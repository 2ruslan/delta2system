package delta2.system.warduinobridge;

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
import delta2.system.warduinobridge.Preferences.PreferencesHelper;

public class BluetoothManager {
    private Context _context;
    private BluetoothAdapter mAdapter;
    private BluetoothSocket mBluetoothSocket;
    private Handler bluetoothIn;
    final int handlerState = 0;
    private ConnectedThread mConnectedThread;

    public BluetoothManager(Context c){
        _context = c;


        connect();
    }

    public void connect(){
        if(!PreferencesHelper.getDeviceAddress().equals("") ){


            bluetoothIn = new Handler() {
                StringBuilder sb = new StringBuilder();

                public void handleMessage(android.os.Message msg) {
                    if (msg.what == handlerState) {
                        String readMessage = (String) msg.obj;
                        sb.append(readMessage);
                        if(sb.toString().contains("\r")) {
                        //    MediatorMD.sendText("0", sb.toString().replace("<br>", "\r\n"));
                            sb = new StringBuilder();
                        }
                        }
                    }

            };

            mAdapter = BluetoothAdapter.getDefaultAdapter();


            BluetoothDevice device = mAdapter.getRemoteDevice(PreferencesHelper.getDeviceAddress());
            UUID u =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            try {
                mBluetoothSocket = device.createRfcommSocketToServiceRecord(u);

                mBluetoothSocket.connect();

                mConnectedThread = new ConnectedThread(mBluetoothSocket);
                mConnectedThread.start();

            }catch (Exception e){
                L.log.error("", e);
            }
        }
    }


    public void BTSendText(String txt) {
        if (txt.length() > 0) {
            mConnectedThread.write(txt);
        }
    }

    public void CheckMessage(String inTxt, boolean repeatCmd, boolean isSilent, String msgId) {
        BTSendText(inTxt);
    }


    public void CheckMessage(String inTxt, String msgId) {
        CheckMessage(inTxt, false, false, msgId);
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
                }
            }
     }

     private void chekConnection(){

     }

    OnlineTimertask _TimerTask ;
    Timer _Timer;

    private  void initTimertask(){
        _TimerTask = new OnlineTimertask(this);
        _Timer = new Timer();

        _Timer.schedule(_TimerTask, 10000,  (int)(5 * 60 *1000));
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

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
import delta2.system.common.commands.Command;
import delta2.system.common.interfaces.messages.IRequestSendMessage;
import delta2.system.common.messages.MessageCommand;
import delta2.system.common.messages.MessageText;
import delta2.system.warduinobridge.Preferences.PreferencesHelper;

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

                        requestSendMessage(parts[0].replace("<br>", "\r\n"));

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

    private void requestSendMessage(String msg) {
        if (msg.startsWith("\n"))
            msg = msg.substring(1);
        if (msg.startsWith("\r"))
            msg = msg.substring(1);

        if (requestSendMessage != null)
            if (msg.startsWith("<cmd>")) {
                requestSendMessage.RequestSendMessage(
                        new MessageCommand(new Command(msg.replace("<cmd>", "").trim(), "")));
            } else {
                requestSendMessage.RequestSendMessage(
                        new MessageText(msg)
                );
            }
    }


    public void connect(){
        if(!PreferencesHelper.getDeviceAddress().equals("") ){

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
                UUID u =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

                mBluetoothSocket = device.createRfcommSocketToServiceRecord(u);

                mBluetoothSocket.connect();

                mConnectedThread = new ConnectedThread(mBluetoothSocket);
                mConnectedThread.start();

                needReconect = false;

            }catch (Exception e){
                L.log.error("", e);
                needReconect = true;
            }
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

        _Timer.schedule(_TimerTask, 0,  60 *1000);
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

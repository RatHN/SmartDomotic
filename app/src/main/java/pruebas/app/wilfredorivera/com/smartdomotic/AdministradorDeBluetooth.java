package pruebas.app.wilfredorivera.com.smartdomotic;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Neri Ortez on 15/03/2017.
 */

public class AdministradorDeBluetooth {
    public static final String DEVICE_ADDRESS_KEY = "DEVICE_ADDRESS";
    private static final int MESSAGE_READ = 10;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String TAG = "smartdomotic";
    private Context mContext;
    private Handler mHandler;
    private BluetoothDevice mDevice;
    private SharedPreferences mPref;
    private ThreadConectarBluetooth mThreadConectarBluetooth;
    private ThreadTrabajoBluetooth mTrabajoThread;


    AdministradorDeBluetooth(Context context, Handler handler) {
        mPref = context.getSharedPreferences("preferences", MODE_PRIVATE);
        mContext = context;
        mHandler = handler;
    }

    String getDireccionMAC() {
        return mPref.getString(DEVICE_ADDRESS_KEY, "null");
    }

    void conectar(BluetoothDevice device) {
        desconectar();
        mDevice = device;
        mThreadConectarBluetooth = new ThreadConectarBluetooth(mDevice);
        mThreadConectarBluetooth.start();
    }

    void reconetar() {
        mThreadConectarBluetooth = new ThreadConectarBluetooth(mDevice);
        mThreadConectarBluetooth.start();
    }

    void escribir(byte[] data) {
        mTrabajoThread.write(data);
    }

    void desconectar() {
        if (mThreadConectarBluetooth.isAlive()) {
            mThreadConectarBluetooth.cancelar();
        }
        if (mTrabajoThread.isAlive()) {
            mTrabajoThread.cancelar();
        }
    }


    private void empezarLectura(BluetoothSocket mmSocket) {
        mTrabajoThread = new ThreadTrabajoBluetooth(mmSocket);
        mTrabajoThread.start();
    }

    private void guardarDispositivoBluetooth(String address) {
        mPref.edit().putString(DEVICE_ADDRESS_KEY, address).apply();
    }

    private void mensajeRecibido(byte[] buffer, int bytes) {
        ((SmartDomoticActivity) mContext).mensajeRecibido(buffer, bytes);
    }

    private class ThreadConectarBluetooth extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        ThreadConectarBluetooth(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Creación de Socket fallido", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "No se pudo conectar con el Socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            guardarDispositivoBluetooth(mmDevice.getAddress());
            empezarLectura(mmSocket);
        }

        /**
         * Cerrar toda conexión y liberar recursos
         */
        void cancelar() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }

    }

    private class ThreadTrabajoBluetooth extends Thread {
        private InputStream mmInStream;
        private OutputStream mmOutStream;
        private BluetoothSocket mmSocket;

        ThreadTrabajoBluetooth(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            mmSocket = socket;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
//                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();

                    mensajeRecibido(buffer, bytes);
                } catch (IOException e) {
                    break;
                }
            }
        }

        /**
         * Escribir en el socket
         */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(mContext, "Error en la comunicacion: ENVIO", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Cerrar toda conexión y liberar recursos
         */
        void cancelar() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}

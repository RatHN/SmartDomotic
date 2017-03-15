package pruebas.app.wilfredorivera.com.smartdomotic;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class LisdaDeDispositivos extends ListActivity {
    static String Direccion_MAC = null;
    public String NAME = "smartdomotic";
    public UUID MY_UUID = UUID.randomUUID();
    public String TAG = "smartdomotic";
    private BluetoothAdapter mBluetoothAdapter = null;
    private SharedPreferences pref;

    private ArrayList<BluetoothDevice> dispocitivosconectados;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> Arraybluetooth = new ArrayAdapter<String>(this, R.layout.listaitems);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        Set<BluetoothDevice> lista = mBluetoothAdapter.getBondedDevices();

        for (BluetoothDevice dispositivo : lista) {
            dispocitivosconectados.add(dispositivo);
        }

        if (dispocitivosconectados.size() > 0) {
            for (BluetoothDevice dispositivo : dispocitivosconectados) {
                String nombreBt = dispositivo.getName();
                String nombreMAC = dispositivo.getAddress();
                Arraybluetooth.add(nombreBt + "\n" + nombreMAC);
            }
        }

        setListAdapter(Arraybluetooth);
        pref = getSharedPreferences("preferences", MODE_PRIVATE);
    }

    //
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        new ConnectThread(dispocitivosconectados.get(position), position).run();
    }

    private void guardarDispositivoBluetooth(int index) {
        pref.edit().putInt("DEVICE", index).apply();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final int index;

        public ConnectThread(BluetoothDevice device, int index) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
            this.index = index;
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
            mBluetoothAdapter.cancelDiscovery();

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
            guardarDispositivoBluetooth(index);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

}
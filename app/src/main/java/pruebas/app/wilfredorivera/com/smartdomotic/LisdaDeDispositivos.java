package pruebas.app.wilfredorivera.com.smartdomotic;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static pruebas.app.wilfredorivera.com.smartdomotic.AdministradorDeBluetooth.DEVICE_ADDRESS_KEY;


public class LisdaDeDispositivos extends ListActivity {
    static String Direccion_MAC = null;
    public String NAME = "smartdomotic";
    public UUID MY_UUID = UUID.randomUUID();
    public String TAG = "smartdomotic";
    private BluetoothAdapter mBluetoothAdapter = null;
    private SharedPreferences pref;

    private ArrayList<BluetoothDevice> dispocitivosconectados = new ArrayList<>();
    private AlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = new AlertDialog.Builder(this)
                .setTitle("Conectando").setView(new ProgressBar(getApplicationContext())).create();

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
//        new ConnectThread(dispocitivosconectados.get(position), position).start();
        new ConnectTask(dispocitivosconectados.get(position)).execute();
    }

    private void guardarDispositivoBluetooth(String address) {
        pref.edit().putString(DEVICE_ADDRESS_KEY, address).apply();
        Log.i(TAG, "guardarDispositivoBluetooth: Se guardo el sipositivo: " + address);
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
            Log.i(TAG, "run: Started");

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.i(TAG, "run: Conectando");
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.i(TAG, "run: No se pudo conectar");
                    Toast.makeText(getApplicationContext(), "Dispositivo fuera de alcance", Toast.LENGTH_SHORT).show();
                } catch (IOException closeException) {
                    Log.e(TAG, "No se pudo conectar con el Socket", closeException);
                }
                Toast.makeText(getApplicationContext(), "Conexión exitosa, vuelve atras para continuar", Toast.LENGTH_SHORT).show();
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            guardarDispositivoBluetooth(mmDevice.getAddress());
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

    private class ConnectTask extends AsyncTask<BluetoothDevice, Void, Boolean> {
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        ConnectTask(BluetoothDevice device) {
            super();
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

        @Override
        protected void onPostExecute(Boolean result) {
            mDialog.dismiss();
            if (result) {
                Toast.makeText(getApplicationContext(), "Conexión exitosa, vuelve atras para continuar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Dispositivo fuera de alcance", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(BluetoothDevice... voids) {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();
            Log.i(TAG, "run: Started");
            publishProgress();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.i(TAG, "run: Conectando");
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                Log.i(TAG, "No se pudo conectar con el Socket", connectException);
                try {
                    Class<?> clazz = mmSocket.getRemoteDevice().getClass();
                    mmSocket.close();
                    Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
                    Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                    Object[] params = new Object[]{Integer.valueOf(1)};
                    BluetoothSocket sockFallback = (BluetoothSocket) m.invoke(mmSocket.getRemoteDevice(), params);
                    sockFallback.connect();
                    mmSocket = sockFallback;
                } catch (Exception e2) {
                    Log.e(TAG, "Couldn't fallback while establishing Bluetooth connection.", e2);
//                    throw new IOException(e2.getMessage());
                    return false;
                }
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            guardarDispositivoBluetooth(mmDevice.getAddress());
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}
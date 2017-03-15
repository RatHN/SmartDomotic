package pruebas.app.wilfredorivera.com.smartdomotic;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Creada por Nery Ortez. Marzo 2016
 */

abstract class SmartDomoticActivity extends AppCompatActivity {
    static final int activa_bluetooth = 1;
    static final int activa_conexion = 2;

    AdministradorDeBluetooth mAdministradorBluetooth;
    BluetoothAdapter miBluetoothAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdministradorBluetooth = new AdministradorDeBluetooth(this, null);

        miBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Bluetooth
        if (miBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "El dispositivo bluetooth no está encendido", Toast.LENGTH_SHORT).show();
        } else if (!miBluetoothAdapter.isEnabled()) {
            Intent activarBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activarBtIntent, activa_bluetooth);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdministradorBluetooth.desconectar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdministradorBluetooth.conectar(                               //Enlazamos el administrador del Bluetooth
                miBluetoothAdapter.getRemoteDevice(                     //Con el dispositivo
                        mAdministradorBluetooth.getDireccionMAC()));    //Que se guardó en memoria

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case activa_bluetooth:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth conectado", Toast.LENGTH_SHORT).show();
                } else {
//                        Toast.makeText(getApplicationContext(), "Bluetooth no está conectado, se cerrará la aplicación", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(this)
                            .setTitle("Importante")
                            .setMessage("Tenés que activar el bluetooth sí o sí")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent activarBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(activarBtIntent, activa_bluetooth);
                                }
                            })
                            .show();
                }
                break;
        }
    }

    abstract void mensajeRecibido(byte[] buffer, int bytes);

}

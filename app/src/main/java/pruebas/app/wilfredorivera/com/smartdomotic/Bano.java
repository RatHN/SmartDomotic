package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Bano extends SmartDomoticActivity {

    final String LUZ = "LUZB";
    final String TOMA = "TOMAB";
    final String ON = "1\n";
    final String OFF = "0\n";

    Switch lb, tcb;
    private TextView estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bano);
        lb = (Switch) findViewById(R.id.lb);
        tcb = (Switch) findViewById(R.id.tcb);
        estado = ((TextView) findViewById(R.id.estadoTxt));

        lb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 byte[] data;
                 if (b) data = (LUZ + ON).getBytes();
                 else data = (LUZ + OFF).getBytes();

                 mAdministradorBluetooth.escribir(data);
             }
         });
         tcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 byte[] data;
                 if (b) data = (TOMA + ON).getBytes();
                 else data = (TOMA + OFF).getBytes();

                 mAdministradorBluetooth.escribir(data);
             }
         });
    }

    @Override
    void mensajeRecibido(byte[] buffer, int bytes) {

    }

    @Override
    void estadoBluetooth(boolean listo) {
        if (listo) {
            estado.setText("Estado: Listo");
        } else {
            estado.setText("Esperando Conexión");
        }
    }

}

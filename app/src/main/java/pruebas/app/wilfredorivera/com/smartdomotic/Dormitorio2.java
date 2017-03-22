package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Dormitorio2 extends SmartDomoticActivity {

    private static final String OFF = "0\n";
    private static final String ON = "1\n";
    private static final String TOMA = "TOMAD2";
    private static final String LUZ = "LUZD2";
    private static final String VENT = "VENTD2";
    private static final String PI = "PID2";
    Switch ld2, tcd2, vd2, pi2;
    private TextView estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitorio2);
        estado = ((TextView) findViewById(R.id.estadoTxt));

        tcd2 = ((Switch) findViewById(R.id.tcd2));
        tcd2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (TOMA + ON).getBytes();
                else data = (TOMA + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        ld2 = ((Switch) findViewById(R.id.ld2));
        ld2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (LUZ + ON).getBytes();
                else data = (LUZ + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        vd2 = ((Switch) findViewById(R.id.vd2));
        vd2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (VENT + ON).getBytes();
                else data = (VENT + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        pi2 = ((Switch) findViewById(R.id.pi2));
        pi2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (PI + ON).getBytes();
                else data = (PI + OFF).getBytes();

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

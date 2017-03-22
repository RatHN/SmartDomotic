package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Dormitorio1 extends SmartDomoticActivity {

    private static final String OFF = "0\n";
    private static final String ON = "1\n";
    private static final String TOMA = "TOMAD1";
    private static final String LUZ = "LUZD1";
    private static final String VENT = "VENTD1";
    private static final String TV = "TVD1";

    Switch ld1, tcd1, vd1, tvd1;
    private TextView estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitorio1);

        estado = ((TextView) findViewById(R.id.estadoTxt));

        tcd1 = ((Switch) findViewById(R.id.tcd1));
        tcd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (TOMA + ON).getBytes();
                else data = (TOMA + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        ld1 = ((Switch) findViewById(R.id.ld1));
        ld1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (LUZ + ON).getBytes();
                else data = (LUZ + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        vd1 = ((Switch) findViewById(R.id.vd1));
        vd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (VENT + ON).getBytes();
                else data = (VENT + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        tvd1 = ((Switch) findViewById(R.id.tvd1));
        tvd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (TV + ON).getBytes();
                else data = (TV + OFF).getBytes();

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

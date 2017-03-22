package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Sala1 extends SmartDomoticActivity {

    private static final String OFF = "0\n";
    private static final String ON = "1\n";
    private static final String TOMA = "TOMAS1";
    private static final String LUZ = "LUZS1";
    private static final String VENT = "VENTS1";

    Switch ls1,tcs1,vds1;
    private TextView estado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala1);

        estado = ((TextView) findViewById(R.id.estadoTxt));

        ls1 = ((Switch) findViewById(R.id.ls1));
        ls1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (LUZ + ON).getBytes();
                else data = (LUZ + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        tcs1 = ((Switch) findViewById(R.id.tcs1));
        tcs1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (TOMA + ON).getBytes();
                else data = (TOMA + OFF).getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
        vds1 = ((Switch) findViewById(R.id.vs1));
        vds1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = (VENT + ON).getBytes();
                else data = (VENT + OFF).getBytes();

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

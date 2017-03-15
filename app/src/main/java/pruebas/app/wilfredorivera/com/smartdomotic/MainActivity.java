package pruebas.app.wilfredorivera.com.smartdomotic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends SmartDomoticActivity {


    private final String PUERTA_MENSAJE = "PUERTA";
    private final String CORREDOR_MENSAJE = "CORREDOR";
    Button dormitorio1, dormitorio2, Sala, bano, btconectar;
    Switch puerta, corredorL;
    boolean conexion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Interruptores secundarios
        puerta = (Switch) findViewById(R.id.puerta);
        corredorL = (Switch) findViewById(R.id.corredorL);

        //Botones principales
        dormitorio1 = (Button) findViewById(R.id.dormitorio1);
        dormitorio2 = (Button) findViewById(R.id.dormitorio2);
        Sala = (Button) findViewById(R.id.Sala);
        bano = (Button) findViewById(R.id.bano);
        btconectar = (Button) findViewById(R.id.btconectar);


        //Setear funciones de botones y switches
        btconectar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (conexion) {
                    //Desconectar
                } else {
                    //Conectar
                    Intent abrirlista = new Intent(MainActivity.this, LisdaDeDispositivos.class);
                    startActivityForResult(abrirlista, activa_conexion);
                }
            }
        });

        dormitorio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Dormitorio1.class);
                startActivity(i);
            }
        });

        dormitorio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Dormitorio2.class);
                startActivity(i);
            }
        });

        Sala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Sala1.class);
                startActivity(i);
            }
        });

        bano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Bano.class);
                startActivity(i);
            }
        });

        puerta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = "PUERTA:1".getBytes();
                else data = "PUERTA:0".getBytes();

                mAdministradorBluetooth.escribir(data);

            }
        });

        corredorL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                byte[] data;
                if (b) data = "CORREDOR:1".getBytes();
                else data = "CORREDOR:0".getBytes();

                mAdministradorBluetooth.escribir(data);
            }
        });
    }


    @Override
    void mensajeRecibido(byte[] buffer, int bytes) {

    }
}

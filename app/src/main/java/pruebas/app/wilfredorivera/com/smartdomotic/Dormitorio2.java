package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.widget.Switch;

public class Dormitorio2 extends SmartDomoticActivity {

    Switch ld2, tcd2, vd2, pi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitorio2);
    }

    @Override
    void mensajeRecibido(byte[] buffer, int bytes) {

    }

}

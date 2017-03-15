package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.widget.Switch;

public class Dormitorio1 extends SmartDomoticActivity {

    Switch ld1, tcd1, vd1, tvd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitorio1);
    }

    @Override
    void mensajeRecibido(byte[] buffer, int bytes) {

    }
}

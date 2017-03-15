package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.widget.Switch;

public class Sala1 extends SmartDomoticActivity {

    Switch ls1,tcs1,vds1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala1);
    }

    @Override
    void mensajeRecibido(byte[] buffer, int bytes) {

    }


}

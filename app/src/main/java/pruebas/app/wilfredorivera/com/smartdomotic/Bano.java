package pruebas.app.wilfredorivera.com.smartdomotic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

public class Bano extends AppCompatActivity {

    Switch lb, tcb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bano);
        lb = (Switch) findViewById(R.id.lb);
        tcb = (Switch) findViewById(R.id.tcb);

        // lb.setOnClickListener(this);
        // tcb.setOnClickListener(this);
    }
}

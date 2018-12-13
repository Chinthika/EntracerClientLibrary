package entracer.chinthika.com.entracerclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import entracer.chinthika.com.entracerlibrary.EntracerLib;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EntracerLib elib = new EntracerLib("494059203c897d994117352519fd6e49ad");
        Log.i("Result", elib.getPersonByEmail("chinthika.15@itfac.mrt.ac.lk").getFirst_name());
        Log.i("Result", elib.getAllPersons()[1].getFirst_name());
    }
}

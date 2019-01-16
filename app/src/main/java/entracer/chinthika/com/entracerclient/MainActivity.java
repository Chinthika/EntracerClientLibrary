package entracer.chinthika.com.entracerclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import entracer.chinthika.com.entracerlibrary.EntracerLib;
import entracer.chinthika.com.entracerlibrary.Person;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EntracerLib elib = new EntracerLib("494059203c897d994117352519fd6e49ad", true);
        Log.i("Result", elib.getPersonByEmail("chinthika.15@itfac.mrt.ac.lk").getFirst_name());
        Log.i("Result", elib.getPersonById("5bea7e259bdf8e3cacf07d01").getFirst_name());
        elib.deletePersonById("5bea7dbc9bdf8e3cacf07cf3");
        elib.getOrganisationById("5bb300ba9bdf8e226bb789ac");
        elib.getAllOrganisations();
        Log.i("Result", elib.getAllPersons()[1].getFirst_name());
        elib.deleteOrganisationById("5bc597759bdf8e368f5a7810");
        Person p = new Person();
        p.setFirst_name("ccccccccccqqqqq");
        p.setEmail("chathura14gggrrt@gmail.com");
        p.setType("Lead");
        p.setDo_not_call("true");
        Person created = elib.createPerson(p);
        p.setCity("Tangalle");
        elib.updatePerson(created.getId(), p);

    }
}

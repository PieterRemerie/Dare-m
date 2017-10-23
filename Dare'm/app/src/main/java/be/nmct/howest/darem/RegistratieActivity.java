package be.nmct.howest.darem;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.nmct.howest.darem.Model.Registratie;
import be.nmct.howest.darem.databinding.ActivityLoginBinding;
import be.nmct.howest.darem.databinding.ActivityRegistratieBinding;


public class RegistratieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registratie);

        //Titel
        setTitle("SIGN UP");

        ActivityRegistratieBinding activityRegistratieBinding = DataBindingUtil.setContentView(this, R.layout.activity_registratie);

        //lege login
        final Registratie login = new Registratie("", "", "", "");
        activityRegistratieBinding.setRegistratie(login);
    }
}

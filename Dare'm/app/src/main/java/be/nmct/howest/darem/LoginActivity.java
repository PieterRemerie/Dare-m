package be.nmct.howest.darem;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //lege login
        final Login login = new Login("","");
        activityMainBinding.setLogin(login);

    }
}

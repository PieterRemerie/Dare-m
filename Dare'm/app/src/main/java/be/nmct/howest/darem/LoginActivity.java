package be.nmct.howest.darem;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;

import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.database.DatabaseHelper;
import be.nmct.howest.darem.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        //lege login
        final Login login = new Login("","");
        activityLoginBinding.setLogin(login);

        Button logIn = (Button) findViewById(R.id.btnLogin);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChallengeActivity.class);
                startActivity(intent);
            }
        });
        Button signUP = (Button) findViewById(R.id.btnSignUp);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegistratieActivity.class);
                startActivity(intent);
            }
        });


    }
}

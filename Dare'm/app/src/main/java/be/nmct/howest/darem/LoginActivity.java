package be.nmct.howest.darem;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.database.DatabaseHelper;
import be.nmct.howest.darem.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginFB;
    JSONObject profileInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        View view = getLayoutInflater().inflate(R.layout.activity_login, null);

        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

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


        // READ permissions voor FACEBOOK gegevens op te laden
        loginFB = (LoginButton) findViewById(R.id.btnLoginFB);
        loginFB.setReadPermissions("public_profile");
        loginFB.setReadPermissions("user_birthday");
        loginFB.setReadPermissions("user_friends");


        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("onSuccesFACEBOOK" , loginResult.getRecentlyGrantedPermissions().toString());
                Intent intent = new Intent(LoginActivity.this, ChallengeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.i("ERROR", error.getMessage());
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



}

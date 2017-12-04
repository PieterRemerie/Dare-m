package be.nmct.howest.darem;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import be.nmct.howest.darem.Loader.HttpGetRequest;
import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.database.Contract;
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
        final Login login = new Login("", "");
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
        loginFB.setReadPermissions("email");


        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("onSuccesFACEBOOK", loginResult.getRecentlyGrantedPermissions().toString());
                new SendPost(loginResult.getAccessToken().getToken()).execute();
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

    public void saveNewUser() {

        ContentValues values = new ContentValues();
        String url = "https://darem.herokuapp.com/userprofile?authToken=" + AccessToken.getCurrentAccessToken().getUserId();
        String result = null;
        JSONArray jsonUser = null;
        HttpGetRequest getRequest = new HttpGetRequest();

        try {
            result = getRequest.execute(url).get();

            if(result != null){

                JSONArray jObj = new JSONArray(result);

                String userFirstname = jObj.getJSONObject(0).getString("givenName");
                String userLastname = jObj.getJSONObject(0).getString("familyName");
                String userMail = jObj.getJSONObject(0).getString("email");
                String userImgurl = jObj.getJSONObject(0).getJSONObject("facebook").getString("photo");

                if(userFirstname != null && userLastname != null && userMail != null){
                    values.put(Contract.UserColumns.COLUMN_USER_VOORNAAM, userFirstname );
                    values.put(Contract.UserColumns.COLUMN_USER_NAAM, userLastname);
                    values.put(Contract.UserColumns.COLUMN_USER_EMAIL, userMail);

                }

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    class SendPost extends AsyncTask<String, Void, String>{

        String token;

        public SendPost(String token) {
            this.token = token;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                postRequest();
            } catch (IOException e) {
                Log.i("dd", "doInBackground: fout");
                e.printStackTrace();
                return null;
            }
            return null;
        }

        private void postRequest() throws IOException {

            try{
                URL url = new URL("https://darem.herokuapp.com/users/auth/facebook/token?access_token=" + token);
                Log.i("token", url.toString());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));

                //get userid
                InputStream is = conn.getInputStream();
                int ch;
                StringBuffer userID = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    userID.append((char) ch);
                }
                Log.i("MSG" , userID.toString());

                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.i("ee", "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.i("ee", "IOException: " + e.getMessage());
            }
        }

    }
}

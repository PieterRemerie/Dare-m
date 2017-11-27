package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import be.nmct.howest.darem.Loader.HttpGetRequest;
import be.nmct.howest.darem.Loader.HttpGetRequest;


public class ChallengeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        View view = getLayoutInflater().inflate(R.layout.activity_challenge, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                Intent intent = new Intent();
                switch (item.getItemId()){
                    case R.id.yourchallengesDrawer:
                        intent = new Intent(getApplicationContext(), AddFriendsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.friendsDrawer:
                        intent = new Intent(getApplicationContext(), AddFriendsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.invitesDrawer:
                        intent = new Intent(getApplicationContext(), InviteOverviewActivity.class);
                        startActivity(intent);
                        break;
                }
                drawer.closeDrawers();
                return false;



            }
        });
        View header = navigationView.getHeaderView(0);
        TextView txtUserNaam = (TextView) header.findViewById(R.id.txtUserNaam);
        TextView txtUserMail = (TextView) header.findViewById(R.id.txtUserEmail);

        String url = "https://darem.herokuapp.com/userprofile?authToken=" + AccessToken.getCurrentAccessToken().getUserId();
        String result = null;
        JSONArray jsonUser = null;
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(url).get();

            if (result != null) {

                JSONArray jObj = new JSONArray(result);
                Log.i("Info jsonUser", jObj.getJSONObject(0).toString());
               // Log.i("Info jsonUser", jObj.getJSONObject(0).getString("givenName"));

                String Username = jObj.getJSONObject(0).getString("givenName") + " " + jObj.getJSONObject(0).getString("familyName");
                String Usermail = jObj.getJSONObject(0).getString("email");
                if (Username != null && Usermail != null) {
                    Log.i("USERNAME ", Username);
                    txtUserNaam.setText(Username);
                    txtUserMail.setText(Usermail);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        if (savedInstanceState == null) {
            showChallengesOverviewFragment();
        }
        final FloatingActionButton fabChallenges = (FloatingActionButton) findViewById(R.id.fab_addChallenge);
        fabChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateChallengeActivity.class);
                startActivity(intent);
            }
        });
        final FloatingActionButton fabFriends = (FloatingActionButton) findViewById(R.id.fab_addFriends);
        fabFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddFriendsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void showChallengesOverviewFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChallengeOverviewFragment challengesOverviewFragment = new ChallengeOverviewFragment();


        fragmentTransaction.replace(R.id.framelayout_in_challengeactivity, challengesOverviewFragment);
        fragmentTransaction.commit();
    }
}

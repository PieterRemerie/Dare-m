package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

import be.nmct.howest.darem.Navigation.Navigation;
import be.nmct.howest.darem.auth.AuthHelper;

public class AddFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View view = getLayoutInflater().inflate(R.layout.activity_add_friends, null);
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
                        intent = new Intent(getApplicationContext(), ChallengeActivity.class);
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
                    case R.id.logoutUser:
                        try {
                            AuthHelper.logUserOff(getApplicationContext());
                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();
                            AccessToken.setCurrentAccessToken(null);
                            DeletePreviousDBUser();
                            intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }
                drawer.closeDrawers();
                return false;
            }
        });

        Navigation.setHeaderOfflineData(navigationView, view);

        if(savedInstanceState == null){
            showAddFriendsFragment();
            showAddFriendsAllFragment();
        }

    }

    private void showAddFriendsAllFragment() {

        //FRAGMENT WAARIN ALLE VRIENDEN STAAN DIE AL TOEGEVOEGD ZIJN

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddFriendsAllFragment addFriendsAllFragment = new AddFriendsAllFragment();
        fragmentTransaction.replace(R.id.framelayout2_in_add_friends, addFriendsAllFragment);
        fragmentTransaction.commit();

    }

    private void showAddFriendsFragment() {

        // FRAGMENT WAARIN DE LIJST STAAT VOOR DE VRIENDEN TE ZOEKEN EN TOE TE VOEGEN

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddFriendsFragment addFriendsFragment = new AddFriendsFragment();
        fragmentTransaction.replace(R.id.framelayout1_in_add_friends, addFriendsFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void DeletePreviousDBUser() throws RemoteException, OperationApplicationException {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        //bestaande producten lokaal verwijderen
        ContentProviderOperation contentProviderOperationDelete = ContentProviderOperation.newDelete(be.nmct.howest.darem.provider.Contract.USERS_URI).build();
        operationList.add(contentProviderOperationDelete);
        contentResolver.applyBatch(be.nmct.howest.darem.provider.Contract.AUTHORITY, operationList);
    }
}

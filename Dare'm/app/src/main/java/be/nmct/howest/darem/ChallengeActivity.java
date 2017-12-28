package be.nmct.howest.darem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.firebase.client.Firebase;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import be.nmct.howest.darem.Loader.HttpGetRequest;
import be.nmct.howest.darem.Loader.HttpGetRequest;
import be.nmct.howest.darem.Model.Notification;
import be.nmct.howest.darem.Navigation.Navigation;
import be.nmct.howest.darem.Transforms.CircleTransform;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.database.CategoriesData;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.database.DatabaseHelper;
import be.nmct.howest.darem.database.SaveCategoriesToDBTask;
import be.nmct.howest.darem.database.SaveNewChallengeToDBTask;
import be.nmct.howest.darem.database.SaveNewUserToDBTask;
import be.nmct.howest.darem.firebase.MyFirebaseInstanceIDService;

import com.google.firebase.auth.FirebaseAuthException;

import static be.nmct.howest.darem.provider.Contract.AUTHORITY;


public class ChallengeActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseMessageService";
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveCategories();
        FirebaseMessaging.getInstance().subscribeToTopic(AccessToken.getCurrentAccessToken().getUserId());

        View view = getLayoutInflater().inflate(R.layout.activity_challenge, null);

        setContentView(R.layout.activity_challenge);

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
                switch (item.getItemId()) {
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


        if (savedInstanceState == null) {
            showChallengesOverviewFragment();
        }
        syncDataManual();
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
        checkInternet();
        if (connected) {
            Navigation.setHeader(navigationView, view);
        } else {
            Navigation.setHeaderOfflineData(navigationView, view);
        }

        Toast.makeText(this.getBaseContext(), "welcome: " + AuthHelper.getUsername(this) + " AUTHTOKEN: " + AuthHelper.getAccessToken(this) + " DBToken: " + AuthHelper.getDbToken(this), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit me", true);
            startActivity(intent);
            finish();
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

    private void DeletePreviousDBUser() throws RemoteException, OperationApplicationException {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        //bestaande producten lokaal verwijderen
        ContentProviderOperation contentProviderOperationDelete = ContentProviderOperation.newDelete(be.nmct.howest.darem.provider.Contract.USERS_URI).build();
        operationList.add(contentProviderOperationDelete);
        contentResolver.applyBatch(be.nmct.howest.darem.provider.Contract.AUTHORITY, operationList);
    }

    private void syncDataManual() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        if (AuthHelper.getAccount(getBaseContext()) != null) {
            getBaseContext().getContentResolver().requestSync(AuthHelper.getAccount(getBaseContext()), be.nmct.howest.darem.provider.Contract.AUTHORITY, settingsBundle);
        }
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;
    }

    private void saveCategories() {

        Cursor mData;
        DatabaseHelper helper = DatabaseHelper.getINSTANCE(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        mData = db.query(Contract.CategoryDB.TABLE_NAME,
                new String[]{
                        Contract.CategoryColumns._ID}, null, null, null, null, null);
        mData.getCount();

        if(mData.getCount() <=0){
            String[] cats = CategoriesData.categories;
            String[] imgs = CategoriesData.images;

            ContentValues value = new ContentValues();

            for(int i = 0; i < cats.length ; i++){
                value.put(Contract.CategoryColumns.COLUMN_CATEGORY_NAME, cats[i]);
                value.put(Contract.CategoryColumns.COLUMN_CATEGORY_IMG, imgs[i]);
                Log.i("VALUES", value.toString());

                try {
                    new SaveCategoriesToDBTask(getApplicationContext()).execute(value).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

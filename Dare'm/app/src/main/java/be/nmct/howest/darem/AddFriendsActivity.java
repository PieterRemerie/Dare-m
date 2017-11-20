package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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

public class AddFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.yourchallengesDrawer) {
                    Intent intent = new Intent(getApplicationContext(), ChallengeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.friendsDrawer) {
                    Intent intent = new Intent(getApplicationContext(), AddFriendsActivity.class);
                    startActivity(intent);

                } else if (id == R.id.invitesDrawer) {
                    Intent intent = new Intent(getApplicationContext(), InviteOverviewActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            showAddFriendsFragment();
            showAddFriendsAllFragment();
        }

    }

    private void showAddFriendsAllFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddFriendsAllFragment addFriendsAllFragment = new AddFriendsAllFragment();
        fragmentTransaction.replace(R.id.framelayout2_in_add_friends, addFriendsAllFragment);
        fragmentTransaction.commit();

    }

    private void showAddFriendsFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddFriendsFragment addFriendsFragment = new AddFriendsFragment();
        fragmentTransaction.replace(R.id.framelayout1_in_add_friends, addFriendsFragment);
        fragmentTransaction.commit();


    }
}

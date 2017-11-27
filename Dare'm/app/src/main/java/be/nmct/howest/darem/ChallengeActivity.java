package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ChallengeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if(savedInstanceState == null){
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
    private void showChallengesOverviewFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChallengeOverviewFragment challengesOverviewFragment = new ChallengeOverviewFragment();
        fragmentTransaction.replace(R.id.framelayout_in_challengeactivity, challengesOverviewFragment);
        fragmentTransaction.commit();

    }
}

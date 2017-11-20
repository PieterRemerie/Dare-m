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

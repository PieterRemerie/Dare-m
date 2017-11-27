package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import be.nmct.howest.darem.R;

public class InviteOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_overview);
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
            showInviteOverviewFragment();
        }
    }
    private void showInviteOverviewFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InviteOverviewFragment inviteOverviewFragment = new InviteOverviewFragment();
        fragmentTransaction.replace(R.id.framelayout_in_invite_overview_activity, inviteOverviewFragment);
        fragmentTransaction.commit();

    }
}

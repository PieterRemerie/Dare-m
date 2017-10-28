package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.nmct.howest.darem.R;

public class InviteOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_overview);

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

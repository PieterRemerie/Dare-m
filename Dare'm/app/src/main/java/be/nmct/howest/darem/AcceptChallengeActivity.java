package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AcceptChallengeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_challenge);

        if(savedInstanceState == null){

            showAcceptChallengeFragment();
        }

    }



    private void showAcceptChallengeFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AcceptChallengeFragment acceptChallengeFragment = new AcceptChallengeFragment();
        fragmentTransaction.replace(R.id.framelayout_in_accept_challenge, acceptChallengeFragment);
        fragmentTransaction.commit();

    }
}

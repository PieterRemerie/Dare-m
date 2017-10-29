package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

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

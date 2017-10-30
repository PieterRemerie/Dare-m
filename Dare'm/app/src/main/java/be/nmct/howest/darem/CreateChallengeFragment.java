package be.nmct.howest.darem;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.database.SaveNewChallengeToDBTask;
import be.nmct.howest.darem.databinding.FragmentCreateChallengeBinding;

public class CreateChallengeFragment extends Fragment {

    private FragmentCreateChallengeBinding binding;
    private Challenge newChallenge = new Challenge();
    public CreateChallengeFragment() {
        // Required empty public constructor
    }
    public static CreateChallengeFragment newInstance(String param1, String param2) {
        CreateChallengeFragment fragment = new CreateChallengeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_challenge, container, false);
        View v = binding.getRoot();
        binding.setTest(this);
        binding.setChallenge(newChallenge);
        //lege challenge
        /*final Challenge challenge = new Challenge("","","sport","Pieter");
        binding.setChallenge(challenge);*/

        /*Button logIn = (Button) v.findViewById(R.id.btnCreate);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), newChallenge.getName() + " " + newChallenge.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });*/
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void saveNewChallenge(){
        saveChallengeToDb();
        resetProduct();
        Intent intent = new Intent(getContext(), ChallengeActivity.class);
        startActivity(intent);
    }
    private void saveChallengeToDb(){
        ContentValues values = new ContentValues();
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM, newChallenge.getName());
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION, newChallenge.getDescription());
        executeAsyncTask(new SaveNewChallengeToDBTask(getContext()), values);
    }
    private void resetProduct(){
        newChallenge.setName("");
        newChallenge.setDescription("");
    }

    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}

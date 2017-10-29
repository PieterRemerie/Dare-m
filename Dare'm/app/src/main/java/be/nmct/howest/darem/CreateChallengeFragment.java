package be.nmct.howest.darem;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.databinding.FragmentCreateChallengeBinding;

public class CreateChallengeFragment extends Fragment {
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
        final FragmentCreateChallengeBinding fragmentCreateChallengeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_challenge, container, false);
        View v = fragmentCreateChallengeBinding.getRoot();

        //lege challenge
        final Challenge challenge = new Challenge("","","sport","Pieter");
        fragmentCreateChallengeBinding.setChallenge(challenge);

        Button logIn = (Button) v.findViewById(R.id.btnCreate);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), challenge.getName() + " " + challenge.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });


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
}

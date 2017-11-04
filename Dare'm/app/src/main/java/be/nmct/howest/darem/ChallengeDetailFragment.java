package be.nmct.howest.darem;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.nmct.howest.darem.Model.Challenge;

public class ChallengeDetailFragment extends Fragment {

    TextView textViewTitle;
    TextView textViewDescription;
    Challenge challenge;

    public static ChallengeDetailFragment newInstance() {
        ChallengeDetailFragment fragment = new ChallengeDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challenge = getArguments().getParcelable("challenge");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        getActivity().setTitle("DETAIL");

        textViewTitle = (TextView) v.findViewById(R.id.txtTitle);
        textViewDescription = (TextView) v.findViewById(R.id.txtDescription);

        textViewTitle.setText(challenge.getName());
        textViewDescription.setText(challenge.getDescription());

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

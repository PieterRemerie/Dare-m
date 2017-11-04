package be.nmct.howest.darem.Adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import be.nmct.howest.darem.ChallengeDetailFragment;
import be.nmct.howest.darem.ChallengeOverviewFragment;
import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.R;
import be.nmct.howest.darem.databinding.RowChallengesBinding;

/**
 * Created by Piete_000 on 30/10/2017.
 */

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.Viewholder> {

    private ObservableList<Challenge> challenges = null;
    private Context context;

    public ChallengeAdapter(Context context, ObservableList<Challenge> challenges){
        this.context = context;
        this.challenges = challenges;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowChallengesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_challenges, parent, false);
        ChallengeAdapter.Viewholder vh = new ChallengeAdapter.Viewholder(binding);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        final Challenge challenge = challenges.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChallengesDetailFragment(challenge);
            }
        });
        holder.getBinding().setChallenge(challenge);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if(challenges.size()!= 0){
            final Activity activity = (Activity) context;
            TextView textView1 = (TextView)activity.findViewById(R.id.txtWelcome1);
            textView1.setVisibility(View.GONE);
            TextView textView2 = (TextView)activity.findViewById(R.id.txtWelcome2);
            textView2.setVisibility(View.GONE);
        }
        return challenges.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final RowChallengesBinding binding;

        public Viewholder(RowChallengesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot();
        }
        public RowChallengesBinding getBinding(){
            return binding;
        }

        @Override
        public void onClick(View v) {
        }
    }

    private void showChallengesDetailFragment(Challenge challenge){
        final Activity activity = (Activity) context;
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChallengeDetailFragment challengeDetailFragment = new ChallengeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("challenge", challenge);
        challengeDetailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.framelayout_in_challengeactivity, challengeDetailFragment);
        fragmentTransaction.addToBackStack("challengeDetailFragment");
        fragmentTransaction.commit();
    }
}


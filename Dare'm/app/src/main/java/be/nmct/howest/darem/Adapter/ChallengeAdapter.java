package be.nmct.howest.darem.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
                Toast.makeText(v.getContext(),(String)challenge.getName(), Toast.LENGTH_LONG).show();
            }
        });
        holder.getBinding().setChallenge(challenge);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
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
}


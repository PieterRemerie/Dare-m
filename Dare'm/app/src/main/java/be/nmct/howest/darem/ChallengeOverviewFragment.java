package be.nmct.howest.darem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ChallengeOverviewFragment extends Fragment {
    private RecyclerView recyclerViewChallengeOverview;
    public ChallengeOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_challenge_overview, container, false);
        recyclerViewChallengeOverview = (RecyclerView) v.findViewById(R.id.recyclerviewChallengeOverview);
        recyclerViewChallengeOverview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ChallengeOverviewRecycleViewAdapter adapter = new ChallengeOverviewRecycleViewAdapter();
        recyclerViewChallengeOverview.setAdapter(adapter);
        return v;
    }

    public class ChallengeOverviewRecycleViewAdapter extends RecyclerView.Adapter<ChallengeOverviewFragment.ChallengeOverviewViewHolder>{

        @Override
        public ChallengeOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_challenges, parent, false);
            return new ChallengeOverviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChallengeOverviewViewHolder holder, int position) {
            holder.textViewNaam.setText("naam challenge");
            holder.imageViewCategory.setImageResource(R.mipmap.ic_launcher);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
    public class ChallengeOverviewViewHolder extends RecyclerView.ViewHolder{

        public final TextView textViewNaam;
        public final ImageView imageViewCategory;
        public ChallengeOverviewViewHolder(View view) {
            super(view);
            textViewNaam = (TextView) view.findViewById(R.id.textViewChallenge);
            imageViewCategory = (ImageView) view.findViewById(R.id.categoryImage);
        }
    }
}
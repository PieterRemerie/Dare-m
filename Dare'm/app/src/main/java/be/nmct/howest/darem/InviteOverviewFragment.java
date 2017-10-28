package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class InviteOverviewFragment extends Fragment {

    private RecyclerView recyclerViewInviteOverviewFragment;
    public InviteOverviewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invite_overview, container, false);
        recyclerViewInviteOverviewFragment = (RecyclerView)v.findViewById(R.id.recyclerviewInviteOverview);
        recyclerViewInviteOverviewFragment.setLayoutManager(new LinearLayoutManager(this.getContext()));
        InviteOverviewRecycleViewAdapter adapter = new InviteOverviewRecycleViewAdapter();
        recyclerViewInviteOverviewFragment.setAdapter(adapter);
        return v;
    }
    public class InviteOverviewRecycleViewAdapter extends RecyclerView.Adapter<InviteOverviewFragment.InviteOverviewFragmentViewHolder>{


        @Override
        public InviteOverviewFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invite_overview, parent, false);
            return new InviteOverviewFragmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(InviteOverviewFragmentViewHolder holder, int position) {
            holder.textViewChallengeNaam.setText("naam challenge");
            holder.imageViewCategory.setImageResource(R.mipmap.ic_launcher);
            holder.imageButtonAccepted.setImageResource(R.mipmap.checkmark_accepted);
            holder.imageButtonDeclined.setImageResource(R.mipmap.checkmark_declined);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
    public class InviteOverviewFragmentViewHolder extends RecyclerView.ViewHolder{

        public final ImageView imageViewCategory;
        public final TextView textViewChallengeNaam;
        public final ImageButton imageButtonAccepted;
        public final ImageButton imageButtonDeclined;
        public InviteOverviewFragmentViewHolder(View view) {
            super(view);
            imageViewCategory = (ImageView)view.findViewById(R.id.categoryImage);
            textViewChallengeNaam = (TextView)view.findViewById(R.id.textViewChallenge);
            imageButtonAccepted = (ImageButton)view.findViewById(R.id.btnAcceptInvite);
            imageButtonDeclined = (ImageButton)view.findViewById(R.id.btnDeclineInvite);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInviteDetailFragment();
                }
            });
        }
    }

    private void showInviteDetailFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InviteDetailFragment inviteDetailFragment = new InviteDetailFragment();
        fragmentTransaction.replace(R.id.framelayout_in_invite_overview_activity, inviteDetailFragment);
        fragmentTransaction.commit();

    }
}

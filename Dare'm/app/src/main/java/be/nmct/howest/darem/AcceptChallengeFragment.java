package be.nmct.howest.darem;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by katri on 29/10/2017.
 */

public class AcceptChallengeFragment extends Fragment {

    private RecyclerView recyclerViewAcceptChallenge;

    public AcceptChallengeFragment(){
        //empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_accept_challenge, container, false);

        recyclerViewAcceptChallenge = (RecyclerView) v.findViewById(R.id.recyclerviewAcceptChallenge);
        recyclerViewAcceptChallenge.setLayoutManager(new LinearLayoutManager(this.getContext()));
        AcceptChallengeRecycleViewAdapter adapter = new AcceptChallengeRecycleViewAdapter();
        recyclerViewAcceptChallenge.setAdapter(adapter);

        return v;
    }

    public class AcceptChallengeRecycleViewAdapter extends RecyclerView.Adapter<AcceptChallengeFragment.AcceptChallengeViewHolder>{


        @Override
        public AcceptChallengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_friends_all, parent,false);
            return new AcceptChallengeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AcceptChallengeViewHolder holder, int position) {

            holder.textViewNaam.setText("naam van vriend");
            holder.imageViewFriend.setImageResource(R.mipmap.person);

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public class AcceptChallengeViewHolder extends RecyclerView.ViewHolder  {

        public final TextView textViewNaam;
        public final ImageView imageViewFriend;

        public AcceptChallengeViewHolder(View view) {
            super(view);

            textViewNaam = (TextView) view.findViewById(R.id.textViewAddFriendName);
            imageViewFriend = (ImageView) view.findViewById(R.id.addFriendImage);

        }
    }
}

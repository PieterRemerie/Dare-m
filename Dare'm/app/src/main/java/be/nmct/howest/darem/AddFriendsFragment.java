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

public class AddFriendsFragment extends Fragment {

    private RecyclerView recyclerViewAddFriends;

    public AddFriendsFragment(){
        //empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_friends, container, false);

        recyclerViewAddFriends = (RecyclerView) v.findViewById(R.id.recyclerviewAddFriends);
        recyclerViewAddFriends.setLayoutManager(new LinearLayoutManager(this.getContext()));
        AddFriendsRecycleViewAdapter adapter = new AddFriendsRecycleViewAdapter();
        recyclerViewAddFriends.setAdapter(adapter);

        return v;
    }

    public class AddFriendsRecycleViewAdapter extends RecyclerView.Adapter<AddFriendsFragment.AddFriendsViewHolder>{


        @Override
        public AddFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_friends, parent,false);
            return new AddFriendsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AddFriendsViewHolder holder, int position) {

            holder.textViewNaam.setText("naam van vriend");
            holder.imageViewFriend.setImageResource(R.mipmap.person);

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public class AddFriendsViewHolder extends RecyclerView.ViewHolder  {

        public final TextView textViewNaam;
        public final ImageView imageViewFriend;

        public AddFriendsViewHolder(View view) {
            super(view);

            textViewNaam = (TextView) view.findViewById(R.id.textViewAddFriendName);
            imageViewFriend = (ImageView) view.findViewById(R.id.addFriendImage);

        }
    }
}

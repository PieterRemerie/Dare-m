package be.nmct.howest.darem;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import be.nmct.howest.darem.Loader.Friends;
import be.nmct.howest.darem.Loader.FriendsLoader;

/**
 * Created by katri on 29/10/2017.
 */

public class AddFriendsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerViewAddFriends;
    private AddFriendsRecycleViewAdapter addFriendsRecycleViewAdapter;
    private URL url;
    private Bitmap bmp;

    public AddFriendsFragment(){
        //empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_friends, container, false);

        recyclerViewAddFriends = (RecyclerView) v.findViewById(R.id.recyclerviewAddFriends);

       // AddFriendsRecycleViewAdapter adapter = new AddFriendsRecycleViewAdapter();
     //recyclerViewAddFriends.setAdapter(adapter);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
        recyclerViewAddFriends.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new FriendsLoader(this.getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        addFriendsRecycleViewAdapter = new AddFriendsRecycleViewAdapter(data);
        recyclerViewAddFriends.setAdapter(addFriendsRecycleViewAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class AddFriendsRecycleViewAdapter extends RecyclerView.Adapter<AddFriendsFragment.AddFriendsViewHolder>{

        Cursor mCursorAddFriends;

        AddFriendsRecycleViewAdapter(Cursor cursor){
            this.mCursorAddFriends = cursor;
        }

        @Override
        public AddFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_friends, parent,false);
            return new AddFriendsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AddFriendsViewHolder holder, int position) {


            mCursorAddFriends.moveToPosition(position);

            int colnr1 = mCursorAddFriends.getColumnIndex(Friends.Columns.COLUMN_NAME);
            int colnr2 = mCursorAddFriends.getColumnIndex(Friends.Columns.COLUMN_PICTURE);


            holder.textViewNaam.setText(mCursorAddFriends.getString(colnr1));

            String pictureURL = mCursorAddFriends.getString(colnr2);


            Picasso.with(getContext()).load(pictureURL).resize(60 , 60).into(holder.imageViewFriend);

        }

        @Override
        public int getItemCount() {
            return mCursorAddFriends.getCount();
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

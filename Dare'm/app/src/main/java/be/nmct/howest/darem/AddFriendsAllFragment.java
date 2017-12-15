package be.nmct.howest.darem;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.squareup.picasso.Picasso;

import be.nmct.howest.darem.Loader.AddedFriendsLoader;
import be.nmct.howest.darem.Loader.Friends;
import be.nmct.howest.darem.Transforms.CircleTransform;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.database.FriendLoader;

/**
 * Created by katri on 29/10/2017.
 */

public class AddFriendsAllFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public RecyclerView recyclerViewAddFriendsAll;
    public AddFriendsAllRecycleViewAdapter addFriendsAllRecycleViewAdapter;
    private ContentObserver mObserver;

    public AddFriendsAllFragment(){
        //empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_friends_all, container, false);

        recyclerViewAddFriendsAll = (RecyclerView) v.findViewById(R.id.recyclerviewAddFriendsAll);
        recyclerViewAddFriendsAll.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Luisteren met een  ContentObserver naar een contentprovider
        mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                Log.i("ContentObserverListener", "Content provider changed - FRIENDS");
                getLoaderManager().restartLoader(0, null, AddFriendsAllFragment.this);
            }
        };

        //niet vergeteren te registreren alsook mee te gevev naar wat hij specifiek moet luisteren
        getContext().getContentResolver().registerContentObserver(be.nmct.howest.darem.provider.Contract.FRIENDS_URI, true, mObserver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
       // return new AddedFriendsLoader(this.getContext());
        return new FriendLoader(this.getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        addFriendsAllRecycleViewAdapter = new AddFriendsAllRecycleViewAdapter(data);
        //addFriendsAllRecycleViewAdapter.notifyDataSetChanged();
        recyclerViewAddFriendsAll.setAdapter(addFriendsAllRecycleViewAdapter);
        addFriendsAllRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class AddFriendsAllRecycleViewAdapter extends RecyclerView.Adapter<AddFriendsAllFragment.AddFriendsAllViewHolder>{

        Cursor mCursorAddFriends;

        AddFriendsAllRecycleViewAdapter(Cursor cursor){
            this.mCursorAddFriends = cursor;
        }

        @Override
        public AddFriendsAllViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_friends_all, parent,false);
            return new AddFriendsAllViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AddFriendsAllViewHolder holder, int position) {

            mCursorAddFriends.moveToPosition(position);

            int colnr1 = mCursorAddFriends.getColumnIndex(Contract.FriendsColumns.COLUMN_FRIEND_FULLNAME);
            int colnr2 = mCursorAddFriends.getColumnIndex(Contract.FriendsColumns.COLUMN_FRIEND_PHOTO);

            holder.textViewNaam.setText(mCursorAddFriends.getString(colnr1));

            String pictureURL = mCursorAddFriends.getString(colnr2);
            Picasso.with(getContext()).load(pictureURL).resize(60 , 60).transform(new CircleTransform()).into(holder.imageViewFriend);
        }

        @Override
        public int getItemCount() {
            return mCursorAddFriends.getCount();
        }



    }

    public class AddFriendsAllViewHolder extends RecyclerView.ViewHolder  {

        public final TextView textViewNaam;
        public final ImageView imageViewFriend;

        public AddFriendsAllViewHolder(View view) {
            super(view);

            textViewNaam = (TextView) view.findViewById(R.id.textViewAddFriendName);
            imageViewFriend = (ImageView) view.findViewById(R.id.addFriendImage);

        }
    }
}

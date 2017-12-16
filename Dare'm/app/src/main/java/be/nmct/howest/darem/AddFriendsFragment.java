package be.nmct.howest.darem;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import be.nmct.howest.darem.Loader.Friends;
import be.nmct.howest.darem.Loader.FBFriendsLoader;
import be.nmct.howest.darem.Transforms.CircleTransform;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.database.SaveNewFriendToDBTask;
import be.nmct.howest.darem.database.SaveNewUserToDBTask;

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
        return new FBFriendsLoader(this.getContext());
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

            final int colnr1 = mCursorAddFriends.getColumnIndex(Friends.Columns.COLUMN_NAME);
            int colnr2 = mCursorAddFriends.getColumnIndex(Friends.Columns.COLUMN_PICTURE);
            final int colnr3 = mCursorAddFriends.getColumnIndex(Friends.Columns._ID);
            final String friendName = mCursorAddFriends.getString(colnr1);

            holder.textViewNaam.setText(mCursorAddFriends.getString(colnr1));

            holder.textViewNaam.setText(friendName);

            final String pictureURL = mCursorAddFriends.getString(colnr2);

            Picasso.with(getContext()).load(pictureURL).resize(60 , 60).transform(new CircleTransform()).into(holder.imageViewFriend);

            final String name = mCursorAddFriends.getString(colnr1);
            final String friendID = mCursorAddFriends.getString(colnr3);



            holder.btnAddFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContentValues values = new ContentValues();

                    values.put(Contract.FriendsColumns.COLUMN_FRIEND_FULLNAME, friendName);
                    values.put(Contract.FriendsColumns.COLUMN_FRIEND_PHOTO, pictureURL);

                    executeAsyncTask(new SaveNewFriendToDBTask(getContext()), values);

                    new SendPost(friendID).execute();

                    Toast.makeText(getContext(), name + " is toegevoegd aan uw vrienden", Toast.LENGTH_SHORT).show();
                    getLoaderManager().restartLoader(0, null, AddFriendsFragment.this);
                    Log.i("AddFRIENDS", "COMPLETED");
                }
            });

        }

        @Override
        public int getItemCount() {
            return mCursorAddFriends.getCount();
        }
    }

    public class AddFriendsViewHolder extends RecyclerView.ViewHolder  {

        public final TextView textViewNaam;
        public final ImageView imageViewFriend;
        public final Button btnAddFriends;

        public AddFriendsViewHolder(View view) {
            super(view);

            textViewNaam = (TextView) view.findViewById(R.id.textViewAddFriendName);
            imageViewFriend = (ImageView) view.findViewById(R.id.addFriendImage);
            btnAddFriends = (Button) view.findViewById(R.id.AddFriendPlusImage);

        }
    }


    class SendPost extends AsyncTask<String, Void, String> {

        String friendId;

        public SendPost(String friendId) {
            this.friendId = friendId;
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                PostRequest();
            }
            catch(IOException e) {
                Log.i("AddFriendEXCEPTION", e.getMessage());
                e.printStackTrace();
                return  null;
            }
            return null;
        }

        private void PostRequest()throws IOException {

            try{
                URL url = new URL("https://darem.herokuapp.com/users/friends/add");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //Write
                JSONObject js = new JSONObject();
                js.put("userOne", AccessToken.getCurrentAccessToken().getUserId().toString());
                js.put("userTwo","" + friendId);

                Log.i("jsonobject", js.toString());

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(js.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();

                Log.i("ee", "SEND: DONE");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.i("ee", "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.i("ee", "IOException: " + e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }



}

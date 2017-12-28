package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Loader;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import be.nmct.howest.darem.Loader.Challenge;
import be.nmct.howest.darem.Loader.Friends;
import be.nmct.howest.darem.Loader.InviteChallenge;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.database.CategoriesData;
import be.nmct.howest.darem.database.Contract;


public class InviteOverviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerViewInviteOverviewFragment;
    private InviteOverviewRecycleViewAdapter inviteOverviewRecycleViewAdapter;
    public InviteOverviewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invite_overview, container, false);
        recyclerViewInviteOverviewFragment = (RecyclerView)v.findViewById(R.id.recyclerviewInviteOverview);
        recyclerViewInviteOverviewFragment.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getLoaderManager().initLoader(0, null, this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new InviteChallenge(this.getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        inviteOverviewRecycleViewAdapter = new InviteOverviewRecycleViewAdapter(data);
        recyclerViewInviteOverviewFragment.setAdapter(inviteOverviewRecycleViewAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public class InviteOverviewRecycleViewAdapter extends RecyclerView.Adapter<InviteOverviewFragment.InviteOverviewFragmentViewHolder>{

        Cursor mCursor;

        InviteOverviewRecycleViewAdapter(Cursor cursor){
            this.mCursor = cursor;
        }

        @Override
        public InviteOverviewFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invite_overview, parent, false);
            return new InviteOverviewFragmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(InviteOverviewFragmentViewHolder holder, int position) {
            mCursor.moveToPosition(position);

            int colnr1 = mCursor.getColumnIndex(Challenge.Columns.COLUMN_NAME);
            int colnr2 = mCursor.getColumnIndex(Challenge.Columns.COLUMN_DESCRIPTION);
            int colnr3 = mCursor.getColumnIndex(Challenge.Columns._ID);
            int colnr4 = mCursor.getColumnIndex(Challenge.Columns.COLUMN_CATEGORY);

            int i = checkCategory(mCursor.getString(colnr4));

            holder.textViewChallengeNaam.setText(mCursor.getString(colnr1));
            holder.imageViewCategory.setImageResource(CategoriesData.imgIds[i]);
            holder.challengeId = mCursor.getString(colnr3);
            holder.challengeName = mCursor.getString(colnr1);
            holder.challengeDescription = mCursor.getString(colnr2);
            holder.challengeCategory = "nog geen category";
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }

        public int checkCategory (String cat){
            int result = 0;
            for(int i = 0 ; i < CategoriesData.categories.length ; i++){
                if(CategoriesData.categories[i].contains(cat)){
                    result = i;
                }
            }
            return result;
        }
    }

    public class InviteOverviewFragmentViewHolder extends RecyclerView.ViewHolder{

        public final ImageView imageViewCategory;
        public final TextView textViewChallengeNaam;

        public String challengeId;
        public String challengeName;
        public String challengeDescription;
        public String challengeCategory;

        public InviteOverviewFragmentViewHolder(View view) {
            super(view);
            imageViewCategory = (ImageView)view.findViewById(R.id.categoryImage);
            textViewChallengeNaam = (TextView)view.findViewById(R.id.textViewChallenge);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new SendPost(challengeId).execute();
                    showInviteDetailFragment(challengeName, challengeDescription, challengeCategory, challengeId);
                }
            });
        }
    }

    private void showInviteDetailFragment(String challengeName, String challengeDesc, String challengeCat, String challengeID){
        Bundle bundle = new Bundle();
        bundle.putString("challengeName", challengeName);
        bundle.putString("challengeDesc", challengeDesc);
        bundle.putString("challengeCat", challengeCat);
        bundle.putString("challengeID", challengeID);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InviteDetailFragment inviteDetailFragment = new InviteDetailFragment();
        inviteDetailFragment.setArguments(bundle);
        fragmentTransaction.remove(this);
        fragmentTransaction.replace(R.id.framelayout_in_invite_overview_activity, inviteDetailFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

}

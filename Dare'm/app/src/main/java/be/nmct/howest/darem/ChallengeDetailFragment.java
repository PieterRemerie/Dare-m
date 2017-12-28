package be.nmct.howest.darem;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.icu.util.Freezable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.firebase.client.Firebase;

import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import be.nmct.howest.darem.Loader.Friends;
import be.nmct.howest.darem.Loader.ParticipantsContract;
import be.nmct.howest.darem.Loader.ParticipantsLoader;
import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Model.Notification;
import be.nmct.howest.darem.Transforms.CircleTransform;
import be.nmct.howest.darem.Transforms.RoundedTransformColorBorder;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.database.CategoriesData;
import be.nmct.howest.darem.database.Contract;

public class ChallengeDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    LinearLayout horizontalScrollView;
    View v;
    TextView textViewTitle;
    TextView textViewDescription;
    Challenge challenge;
    ImageView imgCategory;
    private ArrayList<String> friends = new ArrayList<String>();
    private ArrayList<String> friendsId = new ArrayList<String>();
    private JSONArray jsonArray = new JSONArray();
    HashMap<String, String> map = new HashMap<String, String>();
    Button button;
    Button buttonCompleted;

    public static ChallengeDetailFragment newInstance() {
        ChallengeDetailFragment fragment = new ChallengeDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challenge = getArguments().getParcelable("challenge");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = (View) inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        getActivity().setTitle("DETAIL");

        horizontalScrollView = (LinearLayout) v.findViewById(R.id.LinearLayoutImage);
        textViewTitle = (TextView) v.findViewById(R.id.txtTitle);
        textViewDescription = (TextView) v.findViewById(R.id.txtDescription);
        imgCategory = (ImageView) v.findViewById(R.id.imgCategoryDetail);

        textViewTitle.setText(challenge.getName());
        textViewDescription.setText(challenge.getDescription());

        Log.i("Category", challenge.getCategoryId() + "  " + challenge.getCategory());

        int result = -1;
        for(int i = 0; i < CategoriesData.categories.length ; i++){
            if(CategoriesData.categories[i].contains(challenge.getCategory())){
                result = i;
            }
        }

        if(result != -1){
            imgCategory.setImageResource(CategoriesData.imgIds[result]);
            Log.i("Category", challenge.getCategoryId() + "  " + challenge.getCategory());
        }

        Log.i("RESULT", String.valueOf(result));

        button = (Button) v.findViewById(R.id.button2);
        buttonCompleted = (Button) v.findViewById(R.id.btnCompleted);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("challenge", challenge);
                intent.putExtra("friends", map);
                startActivity(intent);
            }
        });

        buttonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new SendPUT().execute();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ParticipantsLoader(this.getContext(), challenge.getDatabaseId());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            Showparticipants(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void Showparticipants(Cursor data) throws JSONException {
        horizontalScrollView.removeAllViews();
        data.moveToFirst();

        int colnr1 = data.getColumnIndex(ParticipantsContract.Columns._ID);
        int colnr2 = data.getColumnIndex(ParticipantsContract.Columns.COLUMN_NAME);
        int colnr3 = data.getColumnIndex(ParticipantsContract.Columns.COLUMN_PICTURE);
        int colnr4 = data.getColumnIndex(ParticipantsContract.Columns.COLUMN_COMPLETED);

        for (int i = 0; i < data.getCount() ; i++) {
            ImageView iv = new ImageView(getContext());

            if(data.getString(colnr4).equals("false")){
                Picasso.with(v.getContext()).load(data.getString(colnr3)).transform(new CircleTransform()).into(iv);
            }else{
                Picasso.with(v.getContext()).load(data.getString(colnr3)).transform(new RoundedTransformColorBorder()).into(iv);
            }

            horizontalScrollView.addView(iv);
            //friends.add(data.getString(colnr2));
            String id = data.getString(colnr1);
            String name = data.getString(colnr2);
            map.put(id, name);
            int width = 150;
            int height = 150;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            iv.setLayoutParams(parms);
            final ViewGroup.MarginLayoutParams lpt =(ViewGroup.MarginLayoutParams) iv.getLayoutParams();
            lpt.setMargins( 10,lpt.topMargin, 10,lpt.bottomMargin);
            iv.setLayoutParams(lpt);
            data.moveToNext();
        }
    }

    class SendPUT extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                putRequest();
            } catch (IOException e) {
                Log.i("dd", "doInBackground: fout");
                e.printStackTrace();
                return null;
            }
            return null;
        }

        private void putRequest() throws IOException {

            try {
                URL url = new URL("https://darem.herokuapp.com/challenge/completed");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //Write
                JSONObject js = new JSONObject();
                js.put("authToken", AuthHelper.getAccessToken(getContext()));
                js.put("challengeID", challenge.getDatabaseId());

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(js.toString());

                os.flush();
                os.close();

                if (conn.getResponseCode() == 200) {
                    getActivity().getLoaderManager().restartLoader(0, null, ChallengeDetailFragment.this);
                }
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
}

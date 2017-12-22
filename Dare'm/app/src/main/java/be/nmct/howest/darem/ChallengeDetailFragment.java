package be.nmct.howest.darem;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;

import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.nmct.howest.darem.Loader.Friends;
import be.nmct.howest.darem.Loader.ParticipantsLoader;
import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Transforms.CircleTransform;
import be.nmct.howest.darem.database.Contract;

public class ChallengeDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    LinearLayout horizontalScrollView;
    View v;
    TextView textViewTitle;
    TextView textViewDescription;
    Challenge challenge;
    private ArrayList<String> friends = new ArrayList<String>();
    private ArrayList<String> friendsId = new ArrayList<String>();
    private JSONArray jsonArray = new JSONArray();
    HashMap<String, String> map = new HashMap<String, String>();
    Button button;

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

        textViewTitle.setText(challenge.getName());
        textViewDescription.setText(challenge.getDescription());

        button = (Button) v.findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("challenge", challenge);
                intent.putExtra("friends", map);
                startActivity(intent);
            }
        });

        return v;
    }

    private void AddUsersToChatDb(){
        String url = "https://gastleshowest2017-dc94f.firebaseio.com/users.json";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ParticipantsLoader(this.getContext(), "5a37765acd376b001433bf16");
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

        int colnr1 = data.getColumnIndex(Friends.Columns._ID);
        int colnr2 = data.getColumnIndex(Friends.Columns.COLUMN_NAME);
        int colnr3 = data.getColumnIndex(Friends.Columns.COLUMN_PICTURE);

        for (int i = 0; i < data.getCount() ; i++) {
            ImageView iv = new ImageView(getContext());
            Picasso.with(v.getContext()).load(data.getString(colnr3)).transform(new CircleTransform()).into(iv);
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
}

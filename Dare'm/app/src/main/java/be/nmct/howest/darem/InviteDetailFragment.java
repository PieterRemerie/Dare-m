package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import be.nmct.howest.darem.Loader.ParticipantsLoader;
import be.nmct.howest.darem.Transforms.CircleTransform;
import be.nmct.howest.darem.Viewmodel.ChallengeOverviewFragmentViewModel;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.databinding.FragmentInviteDetailBinding;


public class InviteDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>   {
    public InviteDetailFragment() {
        // Required empty public constructor
    }

    LinearLayout horizontalScrollView;
    View v;
    TextView ChallengeName;
    TextView ChallengeDescription;
    ImageView ChallengeCategory;
    Button btnAccept;
    Button btnDecline;

    String challengeId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_invite_detail, container, false);

        horizontalScrollView = (LinearLayout) v.findViewById(R.id.LinearLayoutImage);
        ChallengeName = (TextView) v.findViewById(R.id.textviewChallengeNaamInvitesDetail);
        ChallengeDescription = (TextView) v.findViewById(R.id.txtDescription);
        ChallengeCategory = (ImageView) v.findViewById(R.id.imageView5);
        btnAccept = (Button) v.findViewById(R.id.btnAcceptInviteDetail);
        btnDecline = (Button) v.findViewById(R.id.btnDeclineInviteDetail);

        if (getArguments() != null) {
            String challengeName = getArguments().getString("challengeName");
            String challengeDesc = getArguments().getString("challengeDesc");
            String challengeCat = getArguments().getString("challengeCat");
            challengeId = getArguments().getString("challengeID");

            ChallengeName.setText(challengeName);
            ChallengeDescription.setText(challengeDesc);

            getLoaderManager().initLoader(0, null, this);
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPost(challengeId, "accept").execute();
            }
        });

        btnDecline.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPost(challengeId, "decline").execute();
            }
        }));

        return v;


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i("challengeDDDD", challengeId);
        return new ParticipantsLoader(this.getContext(), challengeId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Showparticipants(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void Showparticipants(Cursor data) {
        horizontalScrollView.removeAllViews();
        data.moveToFirst();

        int colnr1 = data.getColumnIndex(Friends.Columns._ID);
        int colnr2 = data.getColumnIndex(Friends.Columns.COLUMN_NAME);
        int colnr3 = data.getColumnIndex(Friends.Columns.COLUMN_PICTURE);

        for (int i = 0; i < data.getCount() ; i++) {
            ImageView iv = new ImageView(getContext());
            Picasso.with(v.getContext()).load(data.getString(colnr3)).transform(new CircleTransform()).into(iv);
            horizontalScrollView.addView(iv);
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

    class SendPost extends AsyncTask<String, Void, String> {

        String challengeId;
        String answer;

        public SendPost(String challengeId, String answer) {
            this.challengeId = challengeId;
            this.answer = answer;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                postRequest();
            } catch (IOException e) {
                Log.i("dd", "doInBackground: fout");
                e.printStackTrace();
                return null;
            }
            return null;
        }

        private void postRequest() throws IOException {

            try {
                URL url = new URL("https://darem.herokuapp.com/users/challenge/response");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //Write
                JSONObject js = new JSONObject();
                js.put("user", AuthHelper.getDbToken(getContext()));
                js.put("challenge", challengeId);
                js.put("response", answer);

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(js.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                if(conn.getResponseCode() == 200){
                    syncDataManual();
                }
                conn.disconnect();
                Log.i("ee", "SEND: DONE");

                Intent myIntent = new Intent( getActivity(), ChallengeActivity.class);
                startActivity(myIntent);
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

    private void syncDataManual() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        if (AuthHelper.getAccount(getContext())!= null) {
            getContext().getContentResolver().requestSync(AuthHelper.getAccount(getContext()), be.nmct.howest.darem.provider.Contract.AUTHORITY, settingsBundle);
        }
    }


}

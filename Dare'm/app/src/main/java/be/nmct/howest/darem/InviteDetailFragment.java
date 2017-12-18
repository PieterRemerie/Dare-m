package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import be.nmct.howest.darem.Viewmodel.ChallengeOverviewFragmentViewModel;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.databinding.FragmentInviteDetailBinding;


public class InviteDetailFragment extends Fragment {
    public InviteDetailFragment() {
        // Required empty public constructor
    }

    TextView ChallengeName;
    TextView ChallengeDescription;
    ImageView ChallengeCategory;
    Button btnAccept;
    Button btnDecline;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View InviteDetailFragmentView = inflater.inflate(R.layout.fragment_invite_detail, container, false);

        ChallengeName = (TextView) InviteDetailFragmentView.findViewById(R.id.textviewChallengeNaamInvitesDetail);
        ChallengeDescription = (TextView) InviteDetailFragmentView.findViewById(R.id.textviewDescriptionInvitesDetail);
        ChallengeCategory = (ImageView) InviteDetailFragmentView.findViewById(R.id.imageViewChallengeIconInvitesDetails);
        btnAccept = (Button) InviteDetailFragmentView.findViewById(R.id.btnAcceptInviteDetail);
        btnDecline = (Button) InviteDetailFragmentView.findViewById(R.id.btnDeclineInviteDetail);

        String challengeName = getArguments().getString("challengeName");
        String challengeDesc = getArguments().getString("challengeDesc");
        String challengeCat = getArguments().getString("challengeCat");
        final String challengeId = getArguments().getString("challengeID");

        ChallengeName.setText(challengeName);
        ChallengeDescription.setText(challengeDesc);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });

        btnDecline.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPost(challengeId, "decline").execute();

            }
        }));

        return InviteDetailFragmentView;


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
                js.put("response", "accept");

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(js.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

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


}

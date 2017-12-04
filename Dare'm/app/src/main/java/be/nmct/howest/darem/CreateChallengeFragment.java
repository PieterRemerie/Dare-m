package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.database.SaveNewChallengeToDBTask;
import be.nmct.howest.darem.databinding.FragmentCreateChallengeBinding;

public class CreateChallengeFragment extends Fragment {

    private FragmentCreateChallengeBinding binding;
    private Challenge newChallenge = new Challenge();
    private ArrayList<String> friendsId = new ArrayList<String>();
    JSONArray jsonArray = new JSONArray();
    private Bundle bundle;
    public CreateChallengeFragment() {
        // Required empty public constructor
    }
    public static CreateChallengeFragment newInstance(String param1, String param2) {
        CreateChallengeFragment fragment = new CreateChallengeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_challenge, container, false);
        View v = binding.getRoot();
        binding.setTest(this);
        binding.setChallenge(newChallenge);
        bundle = getArguments();
        if(bundle != null){
            friendsId = bundle.getStringArrayList("key");
            //friendsId.add(Integer.parseInt(AccessToken.getCurrentAccessToken().getUserId()));
        }
        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void saveNewChallenge(){
        saveChallengeToDb();
        new SendPost().execute();
        this.getActivity().finish();

    }
    private void saveChallengeToDb(){
        ContentValues values = new ContentValues();
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM, newChallenge.getName());
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION, newChallenge.getDescription());
        executeAsyncTask(new SaveNewChallengeToDBTask(getContext()), values);
    }
    private void resetProduct(){
        newChallenge.setName("");
        newChallenge.setDescription("");
    }

    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    class SendPost extends AsyncTask<String, Void, String>{

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

            try{
                URL url = new URL("https://darem.herokuapp.com/challenge/add");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //Write
                JSONObject js = new JSONObject();
                js.put("name", newChallenge.getName());
                js.put("description", newChallenge.getDescription());
                js.put("users", new JSONArray(friendsId));
                js.put("category", "testcategory");
                js.put("creatorId", "id CREATOR");
                js.put("isCompleted", "false");

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(js.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();
                resetProduct();
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
    public void showAddFriendToChallengeFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddFriendToChallengeFragment addFriendToChallengeFragment = new AddFriendToChallengeFragment();
        fragmentTransaction.replace(R.id.framelayout_in_create_challenge_activity, addFriendToChallengeFragment);
        fragmentTransaction.commit();
    }
}

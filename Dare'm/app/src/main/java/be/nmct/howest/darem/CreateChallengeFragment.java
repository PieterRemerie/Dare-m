package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
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
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Model.Login;
import be.nmct.howest.darem.Model.Notification;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.database.CategoriesData;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.database.SaveNewChallengeToDBTask;
import be.nmct.howest.darem.databinding.FragmentCreateChallengeBinding;
import be.nmct.howest.darem.firebase.MyFirebaseMessagingService;

public class CreateChallengeFragment extends Fragment {

    private FragmentCreateChallengeBinding binding;
    private MyFirebaseMessagingService myFirebaseMessagingService;
    private Challenge newChallenge = new Challenge();
    private ArrayList<String> friendsId = new ArrayList<String>();
    private ArrayList<String> friendsNames = new ArrayList<String>();
    private int categoryId;
    private static final String TAG = "FirebaseMessageService";
    JSONArray jsonArray = new JSONArray();
    private Bundle bundle;
    private Bundle innerBundle = new Bundle();

    public CreateChallengeFragment() {
        // Required empty public constructor
    }

    public static CreateChallengeFragment newInstance(String param1, String param2) {
        CreateChallengeFragment fragment = new CreateChallengeFragment();
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_challenge, container, false);
        View v = binding.getRoot();
        bundle = getArguments();

        newChallenge.setCategory("Choose category");
        newChallenge.setDate("Pick the end date");

        if (bundle != null) {
            if (bundle.getStringArrayList("key") != null) {
                friendsId = bundle.getStringArrayList("key");
                friendsNames = bundle.getStringArrayList("names");
                Log.i("CREATE_CHALLENGE", "CHALLENGE FRIENDS TOEGEVOEGD");

            }
            if (bundle.getInt("categoryId") != 0) {
                categoryId = bundle.getInt("categoryId");
                Log.i("CREATE_CHALLENGE", CategoriesData.categories[categoryId]);
                newChallenge.setCategoryId(categoryId);
                newChallenge.setCategory(CategoriesData.categories[categoryId]);
            }
            if(bundle.getString("challengeName") != null){
                newChallenge.setName(bundle.getString("challengeName"));
            }
            if(bundle.getString("challengeDescr") != null){
                newChallenge.setDescription(bundle.getString("challengeDescr"));
            }
            if(bundle.getString("challengeDate") != null){
                newChallenge.setDate(bundle.getString("challengeDate"));
            }
            /*if(bundle.getString("challengeDate") != null){
                newChallenge.setDate(bundle.getString("challengeDate"));
            }*/

            //friendsId.add(Integer.parseInt(AccessToken.getCurrentAccessToken().getUserId()));
        }

        binding.setTest(this);
        binding.setChallenge(newChallenge);

        if (savedInstanceState != null) {
            newChallenge.setName(savedInstanceState.getString("challengeNaam"));
            newChallenge.setDescription(savedInstanceState.getString("challengeDescr"));
            newChallenge.setDate(savedInstanceState.getString("challendeDate"));

        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void saveNewChallenge() {
        saveChallengeToDb();
        new SendPost().execute();
    }


    private void saveChallengeToDb() {

        ContentValues values = new ContentValues();
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM, newChallenge.getName());
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION, newChallenge.getDescription());
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATOR, AuthHelper.getAccessToken(getContext()));
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_DB, "");
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_CATEGORY,CategoriesData.categories[categoryId] );
        values.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_DATE, newChallenge.getDate());

        executeAsyncTask(new SaveNewChallengeToDBTask(getContext()), values);
    }

    private void resetProduct() {
        newChallenge.setName("");
        newChallenge.setDescription("");
        newChallenge.setCategoryId(0);
        newChallenge.setCategory("Choose category");
        newChallenge.setDate("Pick the end date");
    }

    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    class SendPost extends AsyncTask<String, Void, String> {

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
                js.put("category", newChallenge.getCategory());
                js.put("creatorId", AccessToken.getCurrentAccessToken().getUserId());
                js.put("isCompleted", 0);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date mDate = sdf.parse(newChallenge.getDate());
                long timeInMilliseconds = mDate.getTime();

                js.put("endDate", timeInMilliseconds);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(js.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());


                if (conn.getResponseCode() == 200) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
                    for(Integer i = 0; i < friendsId.size(); i++){
                        reference.removeValue();
                        Notification notification = new Notification(friendsId.get(i), newChallenge.getName(), AuthHelper.getUsername(getContext()));
                        reference.setValue(notification);
                    }

                    syncDataManual();
                }
                conn.disconnect();
                resetProduct();
                getActivity().finish();
                Log.i("ee", "SEND: DONE");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.i("ee", "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.i("ee", "IOException: " + e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
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
    public void showAddFriendToChallengeFragment(){

        addItemsToBundle();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddFriendToChallengeFragment addFriendToChallengeFragment = new AddFriendToChallengeFragment();
        addFriendToChallengeFragment.setArguments(innerBundle);
        fragmentTransaction.replace(R.id.framelayout_in_create_challenge_activity, addFriendToChallengeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showAddCategoryToChallengeFragment() {

        addItemsToBundle();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddCategoryToChallengeFragment addCategoryToChallengeFragment = new AddCategoryToChallengeFragment();
        addCategoryToChallengeFragment.setArguments(innerBundle);
        fragmentTransaction.replace(R.id.framelayout_in_create_challenge_activity, addCategoryToChallengeFragment);
        fragmentTransaction.addToBackStack(null).commit();

    }

    public void showAddDateToChallengeFragment(){
        addItemsToBundle();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddDateToChallengeFragment addDateToChallengeFragment = new AddDateToChallengeFragment();
        addDateToChallengeFragment.setArguments(innerBundle);
        fragmentTransaction.replace(R.id.framelayout_in_create_challenge_activity, addDateToChallengeFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void addItemsToBundle(){
        if(newChallenge.getName() !=null){
            innerBundle.putString("challengeName", newChallenge.getName());
        }
        if(newChallenge.getDescription() !=null){
            innerBundle.putString("challengeDescr", newChallenge.getDescription());
        }
        if(!newChallenge.getDate().equals("Pick the end date")){
            innerBundle.putString("challengeDate", newChallenge.getDate());
        }
        if(!newChallenge.getCategory().equals("Choose category")){
            innerBundle.putInt("categoryId", newChallenge.getCategoryId());
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("challengeNaam", newChallenge.getName());
        outState.putString("challengeDescr", newChallenge.getDescription());
        outState.putString("challendeDate", newChallenge.getDate());
    }
}

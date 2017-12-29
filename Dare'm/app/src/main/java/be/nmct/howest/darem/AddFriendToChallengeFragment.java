package be.nmct.howest.darem;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import be.nmct.howest.darem.Adapter.GridviewAdapterFriends;
import be.nmct.howest.darem.Loader.HttpGetRequest;
import be.nmct.howest.darem.Transforms.CircleTransform;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendToChallengeFragment extends Fragment {

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> photos = new ArrayList<String>();
    ArrayList<String> friendId = new ArrayList<String>();
    ArrayList<String> friendsIdChallenge = new ArrayList<String>();
    private Bundle bundle ;

    public AddFriendToChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_friend_to_challenge, container, false);
        final GridView gridView = (GridView) view.findViewById(R.id.gridViewFriends);
        Button btnAddFriendsToChallenge = (Button) view.findViewById(R.id.btnAddFriendsToChallenge);
        String url = "https://darem.herokuapp.com/userprofile?authToken=" + AccessToken.getCurrentAccessToken().getUserId();
        String result = null;
        HttpGetRequest getRequest = new HttpGetRequest();
    bundle = getArguments();
        try{
            result = getRequest.execute(url).get();

            if(result != null){
                JSONArray jObj = new JSONArray(result);
                JSONArray arrayFriends = jObj.getJSONObject(0).getJSONArray("friends");
                for(int i = 0 ; i < arrayFriends.length(); i++){
                    names.add(arrayFriends.getJSONObject(i).getString("name"));
                    photos.add(arrayFriends.getJSONObject(i).getString("photo"));
                    friendId.add(arrayFriends.getJSONObject(i).getString("databaseid"));
                }

            }
        }catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GridviewAdapterFriends gridviewAdapterFriends = new GridviewAdapterFriends(getContext(),names,photos, friendId);
        gridView.setAdapter(gridviewAdapterFriends);

        btnAddFriendsToChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGridView(gridView);
                showCreateChallengeFragment();
            }
        });

        return view;
    }

    private void checkGridView(GridView gridView){
        for(int i = 0; i < gridView.getChildCount(); i++){
            View v = gridView.getChildAt(i);
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkboxAddFriends);
            boolean itemChecked = checkBox.isChecked();
            if(itemChecked == true){
                friendsIdChallenge.add(checkBox.getTag().toString());
            }
        }
    }

    private void showCreateChallengeFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args = new Bundle();
        args.putStringArrayList("key", friendId);
        args.putStringArrayList("names", names);
        if(bundle.getString("challengeName" )!= null){
            args.putString("challengeName",bundle.getString("challengeName" ));
        }
        if(bundle.getString("challengeDescr" )!= null){
            args.putString("challengeDescr",bundle.getString("challengeDescr" ));
        }
        if(bundle.getString("challengeDate" )!= null){
            args.putString("challengeDate",bundle.getString("challengeDate" ));
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CreateChallengeFragment createChallengeFragment = new CreateChallengeFragment();
        createChallengeFragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout_in_create_challenge_activity, createChallengeFragment);
        fragmentTransaction.commit();
    }
}

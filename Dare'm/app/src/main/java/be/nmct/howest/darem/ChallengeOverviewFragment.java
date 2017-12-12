package be.nmct.howest.darem;

import android.accounts.Account;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Viewmodel.ChallengeOverviewFragmentViewModel;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.database.ChallengesLoader;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.databinding.FragmentChallengeOverviewBinding;
import be.nmct.howest.darem.databinding.RowChallengesBinding;

import static be.nmct.howest.darem.provider.Contract.AUTHORITY;


public class ChallengeOverviewFragment extends Fragment{
    private FragmentChallengeOverviewBinding binding;
    private ChallengeOverviewFragmentViewModel challengeOverviewFragmentViewModel;
    private ContentObserver mObserver;

    public ChallengeOverviewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_overview, container, false);
        binding.recyclerviewChallenges.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        challengeOverviewFragmentViewModel = new ChallengeOverviewFragmentViewModel(binding, getContext());
        getActivity().setTitle("OVERVIEW");

        syncDataManual();

        return binding.getRoot();
    }

    private void syncDataManual() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        if (AuthHelper.getAccount(getContext())!= null) {
            getContext().getContentResolver().requestSync(AuthHelper.getAccount(getContext()), be.nmct.howest.darem.provider.Contract.AUTHORITY, settingsBundle);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        loadChallanges();

        //Luisteren met een  ContentObserver naar een contentprovider
        mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                Log.i("ContentObserverListener", "Content provider changed, refresh!!");
                getActivity().getLoaderManager().restartLoader(0, null, challengeOverviewFragmentViewModel);
            }
        };

        //niet vergeteren te registreren alsook mee te gevev naar wat hij specifiek moet luisteren
        getContext().getContentResolver().registerContentObserver(be.nmct.howest.darem.provider.Contract.CHALLENGES_URI, true, mObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadChallanges();
    }

    public void loadChallanges(){
        getLoaderManager().initLoader(0, null, challengeOverviewFragmentViewModel);       //0: id van loader
    }

    public void refreshChallanges(){
        getLoaderManager().destroyLoader(0);
        loadChallanges();
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }
}
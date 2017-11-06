package be.nmct.howest.darem;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
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
import be.nmct.howest.darem.database.ChallengesLoader;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.databinding.FragmentChallengeOverviewBinding;
import be.nmct.howest.darem.databinding.RowChallengesBinding;


public class ChallengeOverviewFragment extends Fragment{
    private FragmentChallengeOverviewBinding binding;
    private ChallengeOverviewFragmentViewModel challengeOverviewFragmentViewModel;
    private ObservableList<Challenge> challengesList;

    public ChallengeOverviewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_overview, container, false);
        binding.recyclerviewChallenges.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        challengeOverviewFragmentViewModel = new ChallengeOverviewFragmentViewModel(binding, getContext());

        getActivity().setTitle("OVERVIEW");


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, challengeOverviewFragmentViewModel);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(0, null, challengeOverviewFragmentViewModel);
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }



}
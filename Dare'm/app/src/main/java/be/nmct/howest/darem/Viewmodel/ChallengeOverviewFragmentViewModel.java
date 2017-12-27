package be.nmct.howest.darem.Viewmodel;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;

import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.database.CategoriesData;
import be.nmct.howest.darem.database.ChallengesLoader;
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.databinding.FragmentChallengeOverviewBinding;

/**
 * Created by Piete_000 on 30/10/2017.
 */

public class ChallengeOverviewFragmentViewModel extends BaseObservable implements LoaderManager.LoaderCallbacks<Cursor> {

    private FragmentChallengeOverviewBinding binding;
    private Context context;

    @Bindable
    private ObservableList<Challenge> challengesList;

    public ChallengeOverviewFragmentViewModel(FragmentChallengeOverviewBinding binding, Context context){
        this.binding = binding;
        this.context = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ChallengesLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        challengesList = new ObservableArrayList<>();
        while(cursor.moveToNext()){
            String[] columns = new String[]{
                    Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM,
                    Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION,
                    Contract.ChallengesColumns.COLUMN_CHALLENGE_DB,
                    Contract.ChallengesColumns.COLUMN_CHALLENGE_CATEGORY,
                    Contract.ChallengesColumns.COLUMN_CHALLENGE_DATE
            };
            Challenge challenge = new Challenge();
            challenge.setName(cursor.getString(cursor.getColumnIndex(Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM)));
            challenge.setDescription(cursor.getString(cursor.getColumnIndex(Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION)));
            challenge.setDatabaseId(cursor.getString(cursor.getColumnIndex(Contract.ChallengesColumns.COLUMN_CHALLENGE_DB)));
            challenge.setDate(cursor.getString(cursor.getColumnIndex(Contract.ChallengesColumns.COLUMN_CHALLENGE_DATE)));
            int i = checkCategory(cursor.getString(cursor.getColumnIndex(Contract.ChallengesColumns.COLUMN_CHALLENGE_CATEGORY)));
            challenge.setCategory(cursor.getString(cursor.getColumnIndex(Contract.ChallengesColumns.COLUMN_CHALLENGE_CATEGORY)));



            challenge.setCategoryId(CategoriesData.imgIds[i]);
            challengesList.add(challenge);
        }
        this.binding.setChallengelist(challengesList);
        notifyPropertyChanged(BR.challengelist);
        //cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imgView, int resource){
        imgView.setImageResource(resource);
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

package be.nmct.howest.darem.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import be.nmct.howest.darem.R;

/**
 * Created by katri on 18/12/2017.
 */

public class CategoriesData {

    private static Context context;

    public CategoriesData(Context context){this.context = context;}

    public final static String[] categories = {

            "Baseball", "Basketball", "Bodybuilding", "Boxing", "Cycling", "Dancing", "Football",
            "Golf", "Running", "Swimming", "Tennis", "Volleybal", "Walking", "Other"

    };
    public final static String[] images = {

            "baseball", "basketball", "bodybuilding", "boxing", "cycling", "dancing", "football",
            "golf", "running", "swimming", "tennis", "volleybal", "walking", "other"

    };

    public static int checkCategory (String cat){
        int result = 0;
        for(int i = 0 ; i < CategoriesData.categories.length ; i++){
            if(CategoriesData.categories[i].contains(cat)){
                result = i;
            }
        }
        return result;
    }


    public final static int[] imgIds = new int[]{R.mipmap.baseball, R.mipmap.basketball, R.mipmap.bodybuilding, R.mipmap.boxing,
            R.mipmap.cycling, R.mipmap.dancing, R.mipmap.football, R.mipmap.golf,
            R.mipmap.running, R.mipmap.swimming, R.mipmap.tennis, R.mipmap.volleyball,
            R.mipmap.walking, R.mipmap.other};




}

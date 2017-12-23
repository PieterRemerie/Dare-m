package be.nmct.howest.darem.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.database.SaveNewFriendToDBTask;
import be.nmct.howest.darem.json.jsonDownloader;
import be.nmct.howest.darem.provider.Contract;
import be.nmct.howest.darem.database.SaveNewChallengeToDBTask;

/**
 * Created by michv on 12/11/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver contentResolver;

    private SyncResult syncResult;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.contentResolver = context.getContentResolver();
    }


    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            this.syncResult = syncResult;
            String jsonData = jsonDownloader.jsonUser(getContext());
            syncChallenges(account, jsonData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void syncChallenges(Account account, String jsonData) throws JSONException, RemoteException, OperationApplicationException {
        try {
            Log.i("SyncAdapter", "syncChallengeItems");
            saveChallengeToDb(jsonData);
            saveFriendsToDb(jsonData);
        } catch (Exception ex) {
            ex.printStackTrace();
            syncResult.stats.numIoExceptions++;
            throw ex;
        }
    }

    private void saveChallengeToDb(String jsonData) throws JSONException {
        Challenge newChallenge;
        try{
            DeletePreviousDB();

            JSONArray jsonArr = new JSONArray(jsonData).getJSONObject(0).getJSONArray("acceptedChallenges");

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject obj = jsonArr.getJSONObject(i);

                newChallenge = new Challenge();

                newChallenge.setName(obj.getString("name"));
                newChallenge.setDescription(obj.getString("description"));
                newChallenge.setCategory(obj.getString("category"));

                ContentValues values = new ContentValues();
                values.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM, newChallenge.getName());
                values.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION, newChallenge.getDescription());
                values.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATOR, AuthHelper.getAccessToken(getContext()));
                values.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_CATEGORY, newChallenge.getCategory());
                values.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_DATE, "momenteel leeg");

                executeAsyncTask(new SaveNewChallengeToDBTask(getContext()), values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR", e.getMessage());
        }
    }

    private void DeletePreviousDB() throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        //bestaande producten lokaal verwijderen
        ContentProviderOperation contentProviderOperationDelete = ContentProviderOperation.newDelete(Contract.CHALLENGES_URI).build();
        operationList.add(contentProviderOperationDelete);
        contentResolver.applyBatch(Contract.AUTHORITY, operationList);
        syncResult.stats.numDeletes++;
    }


    private void saveFriendsToDb(String jsonData) {
        try {
            DeletePreviousDBFriends();

            JSONArray jsonArr = new JSONArray(jsonData).getJSONObject(0).getJSONArray("friends");

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject obj = jsonArr.getJSONObject(i);

                ContentValues values = new ContentValues();

                values.put(be.nmct.howest.darem.database.Contract.FriendsColumns.COLUMN_FRIEND_FULLNAME, obj.getString("name"));
                values.put(be.nmct.howest.darem.database.Contract.FriendsColumns.COLUMN_FRIEND_PHOTO, obj.getString("photo"));

                executeAsyncTask(new SaveNewFriendToDBTask(getContext()), values);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void DeletePreviousDBFriends() throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        //bestaande producten lokaal verwijderen
        ContentProviderOperation contentProviderOperationDelete = ContentProviderOperation.newDelete(Contract.FRIENDS_URI).build();
        operationList.add(contentProviderOperationDelete);
        contentResolver.applyBatch(Contract.AUTHORITY, operationList);
        syncResult.stats.numDeletes++;
    }

    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

}

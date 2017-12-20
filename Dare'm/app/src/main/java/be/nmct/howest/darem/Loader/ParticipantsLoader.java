package be.nmct.howest.darem.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
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

import be.nmct.howest.darem.auth.AuthHelper;
import be.nmct.howest.darem.json.jsonDownloader;

/**
 * Created by michv on 18/12/2017.
 */

public class ParticipantsLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private String challengeId;
    private static Object lock = new Object();

    public ParticipantsLoader(Context context, String challengeId) {
        super(context);
        this.challengeId = challengeId;
    }
    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);

        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        if (mCursor == null) {
            loadData();
        }
        return mCursor;
    }

    private void loadData() {

        synchronized (lock) {
            if (mCursor != null) return;

            String[] mColumnNames = new String[]{
                    Friends.Columns._ID,
                    Friends.Columns.COLUMN_NAME,
                    Friends.Columns.COLUMN_PICTURE
            };

            final MatrixCursor cursor = new MatrixCursor(mColumnNames);


            String jsonData = jsonDownloader.jsonChallenge(this.getContext(), challengeId);

            try {

                if (jsonData != null) {
                    MatrixCursor.RowBuilder row;
                    JSONArray jsonArr = new JSONArray(jsonData).getJSONObject(0).getJSONArray("usersArray");


                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject obj = jsonArr.getJSONObject(i).getJSONObject("facebook");

                        row = cursor.newRow();
                        row.add(obj.getString("id"));
                        row.add(obj.getString("name"));
                        row.add(obj.getString("photo"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("ERROR", e.getMessage());
            }
            mCursor = cursor;
        }
    }


}

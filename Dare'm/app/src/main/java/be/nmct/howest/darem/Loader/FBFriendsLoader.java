package be.nmct.howest.darem.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

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
 * Created by katri on 4/11/2017.
 */

public class FBFriendsLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static Object lock = new Object();

    public FBFriendsLoader(Context context) {
        super(context);

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
            try {
                loadData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mCursor;
    }

    private void loadData() throws JSONException {


        synchronized (lock) {

            if (mCursor != null) return;

                String[] mColumnNames = new String[]{
                        Friends.Columns._ID,
                        Friends.Columns.COLUMN_NAME,
                        Friends.Columns.COLUMN_PICTURE
                };

                final MatrixCursor cursor = new MatrixCursor(mColumnNames);


                JSONArray info = RequestLoader.RequestInfoProfile();
                String json = jsonDownloader.jsonUser(getContext());

                try {

                    if (info != null) {
                        int id = 1;
                        MatrixCursor.RowBuilder row;
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject obj = info.getJSONObject(i);
                            if(!json.toString().contains(obj.getString("id"))){
                                row = cursor.newRow();
                                row.add(obj.getString("id"));
                                row.add(obj.getString("name"));

                                String pictureURL = "https://graph.facebook.com/" + obj.get("id") + "/picture?type=large";
                                row.add(pictureURL);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("ERROR", e.getMessage());

                }

                mCursor = cursor;

        }

    }

}

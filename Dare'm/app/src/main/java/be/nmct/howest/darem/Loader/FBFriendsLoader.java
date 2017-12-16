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

/**
 * Created by katri on 4/11/2017.
 */

public class FBFriendsLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static Object lock = new Object();
    AccessToken accessToken = AccessToken.getCurrentAccessToken();


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
                String json = downloadJSON();
                JSONArray jsonArr = new JSONArray(json).getJSONObject(0).getJSONArray("friends");

                try {

                    if (info != null) {
                        int id = 1;
                        MatrixCursor.RowBuilder row;
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject obj = info.getJSONObject(i);
                            if(!jsonArr.toString().contains(obj.getString("id"))){
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


    private String downloadJSON() {
        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        String stringUrl = "https://darem.herokuapp.com/userprofile?authToken=" + AuthHelper.getAccessToken(getContext());
        String result;
        String inputLine;

        try{
            URL myUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            result = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = null;
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }


}

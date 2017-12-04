package be.nmct.howest.darem.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by michv on 3/12/2017.
 */

public class AddedFriendsLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static Object lock = new Object();

    public AddedFriendsLoader(Context context) {
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


            String jsonData = downloadJSON();

            try {

                if (jsonData != null) {
                    MatrixCursor.RowBuilder row;
                    JSONArray jsonArr = new JSONArray(jsonData).getJSONObject(0).getJSONArray("friends");


                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject obj = jsonArr.getJSONObject(i);

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

    private String downloadJSON() {
        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        String stringUrl = "https://darem.herokuapp.com/userprofile?authToken=" + AccessToken.getCurrentAccessToken().getUserId();
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

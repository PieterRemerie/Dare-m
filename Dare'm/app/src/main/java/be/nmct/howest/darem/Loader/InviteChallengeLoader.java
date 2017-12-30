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
import java.util.Calendar;
import java.util.Date;

import be.nmct.howest.darem.auth.AuthHelper;

/**
 * Created by michv on 14/12/2017.
 */

public class InviteChallengeLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static Object lock = new Object();

    public InviteChallengeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null){
            deliverResult(mCursor);
        }
        if(takeContentChanged() || mCursor == null){
            forceLoad();
        }
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
                    Challenge.Columns._ID,
                    Challenge.Columns.COLUMN_NAME,
                    Challenge.Columns.COLUMN_DESCRIPTION,
                    Challenge.Columns.COLUMN_CATEGORY,
                    Challenge.Columns.COLUMN_DATE
            };

            final MatrixCursor cursor = new MatrixCursor(mColumnNames);

            String jsonData = downloadJSON();

            try {
                if (jsonData != null) {
                    MatrixCursor.RowBuilder row;
                    JSONArray json = new JSONArray(jsonData).getJSONObject(0).getJSONArray("challengesArray");

                    for (int i = 0; i <  json.length(); i++) {
                        JSONObject obj = json.getJSONObject(i);

                        if(currentTimeEndOfDay() < Long.parseLong(obj.getString("endDate"))){
                            row = cursor.newRow();
                            row.add(obj.getString("_id"));
                            row.add(obj.getString("name"));
                            row.add(obj.getString("description"));
                            row.add(obj.getString("category"));
                            row.add(obj.getString("endDate"));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("ERROR", e.getMessage());

            }

            mCursor = cursor;

        }

    }


    private static long currentTimeEndOfDay(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long timeToMilliseconds = calendar.getTimeInMillis();
        return timeToMilliseconds;
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

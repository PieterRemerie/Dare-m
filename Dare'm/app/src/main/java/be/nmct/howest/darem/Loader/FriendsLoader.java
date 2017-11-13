package be.nmct.howest.darem.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by katri on 4/11/2017.
 */

public class FriendsLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private static Object lock = new Object();
    AccessToken accessToken = AccessToken.getCurrentAccessToken();


    public FriendsLoader(Context context) {
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


                JSONArray info = RequestLoader.RequestInfoProfile();

                try {

                    if (info != null) {
                        int id = 1;
                        for (int i = 0; i < info.length(); i++) {

                            JSONObject obj = info.getJSONObject(i);

                            MatrixCursor.RowBuilder row = cursor.newRow();
                            row.add(i++);
                            row.add(obj.getString("name"));

                            JSONObject pictureObject = obj.getJSONObject("picture").getJSONObject("data");

                            Log.i("PictureJSON", pictureObject.toString());

                            String pictureURL = pictureObject.getString("url").replace("\\", "");

                            row.add(pictureURL);


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

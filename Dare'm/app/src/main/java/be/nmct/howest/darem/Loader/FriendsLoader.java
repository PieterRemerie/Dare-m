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
    JSONArray jsonArray ;



    public FriendsLoader(Context context) {
        super(context);

    }

    @Override
    protected void onStartLoading() {
        RequestInfoProfile(accessToken);
        if(mCursor != null){
            deliverResult(mCursor);

        }
        if(takeContentChanged() || mCursor == null) {
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

    private void loadData(){



        synchronized(lock){

            if(mCursor != null) return;

            try{

                String[] mColumnNames = new String[]{
                        Friends.Columns._ID,
                        Friends.Columns.COLUMN_NAME
                };

                MatrixCursor cursor = new MatrixCursor(mColumnNames);
                RequestInfoProfile(accessToken);


                if(jsonArray != null){
                    int id = 1;
                    for(int i = 0 ; i < jsonArray.length() ; i++){

                        JSONObject obj = jsonArray.getJSONObject(i);

                        MatrixCursor.RowBuilder row = cursor.newRow();
                        row.add(i++);
                        row.add(obj.getString("name"));


                    }
                }

                mCursor = cursor;

            }
            catch (Exception ex){
                Log.w("myApp", ex.getMessage());
            }
        }

    }

    public void RequestInfoProfile(AccessToken accToken){

        JSONArray bla = null;

        GraphRequest request = GraphRequest.newMeRequest(accToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject profile, GraphResponse response) {



                        Log.i("INFORMATIE", profile.toString());

                        try {
                            // DIT IS DE JSONARRAY MET DE VRIENDENLIJST
                            JSONArray info = profile.getJSONObject("invitable_friends").getJSONArray("data");

                            jsonArray = info;


                            Log.i("BLABLABLA", jsonArray.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("INFORMATIE", "BLABLABLALBA");

                        }


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,invitable_friends");
        parameters.putInt("limit", 500);
        request.setParameters(parameters);
        request.executeAsync();
        

    }


}

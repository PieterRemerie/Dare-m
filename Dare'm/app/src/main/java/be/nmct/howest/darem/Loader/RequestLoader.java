package be.nmct.howest.darem.Loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by katrien on 13/11/2017.
 */

public class RequestLoader {

    public static JSONArray RequestInfoProfile() {

        final JSONArray[] friends = new JSONArray[1];

        AccessToken accToken = AccessToken.getCurrentAccessToken();

/*
        GraphRequest.GraphJSONObjectCallback jsonRequest = new GraphRequest.GraphJSONObjectCallback(){

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    JSONArray info = object.getJSONObject("invitable_friends").getJSONArray("data");

                    friends[0] = info;

                    Log.i("INFORMATIE", info.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
*/

        Bundle args = new Bundle();
        args.putInt("limit", 1000);
        GraphRequest request = new GraphRequest(accToken, "/me/friends", args, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    JSONObject graphObject = response.getJSONObject();

                    JSONArray dataArray = graphObject.getJSONArray("data");

                    friends[0] = dataArray;

                } catch (Exception e) {
                    System.out.println("Exception=" + e);
                    e.printStackTrace();
                }
            }
        });

        request.executeAndWait();

        if(friends[0] != null) {

            Log.i("VRIENDEN", friends[0].toString());
        }


        return friends[0];

    }


}

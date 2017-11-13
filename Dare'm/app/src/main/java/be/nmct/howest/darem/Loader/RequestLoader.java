package be.nmct.howest.darem.Loader;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by katrien on 13/11/2017.
 */

public class RequestLoader {

    public static JSONArray RequestInfoProfile() {

        final JSONArray[] friends = new JSONArray[1];

        AccessToken accToken = AccessToken.getCurrentAccessToken();

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


        GraphRequest request = GraphRequest.newMeRequest(accToken, jsonRequest);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture,invitable_friends");
        parameters.putInt("limit", 500);
        request.setParameters(parameters);
        request.executeAndWait();

        if(friends[0] != null) {

            Log.i("VRIENDEN", friends[0].toString());
        }


        return friends[0];

    }


}

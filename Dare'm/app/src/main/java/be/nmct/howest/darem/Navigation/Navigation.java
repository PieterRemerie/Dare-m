package be.nmct.howest.darem.Navigation;

import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import be.nmct.howest.darem.Loader.HttpGetRequest;
import be.nmct.howest.darem.R;
import be.nmct.howest.darem.Transforms.CircleTransform;

/**
 * Created by michv on 3/12/2017.
 */

public class Navigation {

    public static void setHeader(NavigationView navigationView, View view) {
        View header = navigationView.getHeaderView(0);
        TextView txtUserNaam = (TextView) header.findViewById(R.id.txtUserNaam);
        TextView txtUserMail = (TextView) header.findViewById(R.id.txtUserEmail);
        ImageView imgUser = (ImageView) header.findViewById(R.id.imgUserPhoto);

        String url = "https://darem.herokuapp.com/userprofile?authToken=" + AccessToken.getCurrentAccessToken().getUserId();
        String result = null;
        JSONArray jsonUser = null;
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(url).get();

            if (result != null) {

                JSONArray jObj = new JSONArray(result);
                Log.i("Info jsonUser", jObj.getJSONObject(0).toString());
                // Log.i("Info jsonUser", jObj.getJSONObject(0).getString("givenName"));

                String Username = jObj.getJSONObject(0).getString("givenName") + " " + jObj.getJSONObject(0).getString("familyName");
                String Usermail = jObj.getJSONObject(0).getString("email");
                String Userimgurl = jObj.getJSONObject(0).getJSONObject("facebook").getString("photo");
                if (Username != null && Usermail != null) {
                    Log.i("USERNAME ", Username);
                    txtUserNaam.setText(Username);
                    txtUserMail.setText(Usermail);
                    Picasso.with(view.getContext()).load(Userimgurl).transform(new CircleTransform()).into(imgUser);

                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
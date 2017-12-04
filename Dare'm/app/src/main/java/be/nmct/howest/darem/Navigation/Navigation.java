package be.nmct.howest.darem.Navigation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import be.nmct.howest.darem.database.Contract;
import be.nmct.howest.darem.database.DatabaseHelper;

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

    public static void setHeaderOfflineData(NavigationView navigationView, View view){
        View header = navigationView.getHeaderView(0);
        TextView txtUserNaam = (TextView) header.findViewById(R.id.txtUserNaam);
        TextView txtUserMail = (TextView) header.findViewById(R.id.txtUserEmail);
        ImageView imgUser = (ImageView) header.findViewById(R.id.imgUserPhoto);

        String[] projection =  {
            Contract.UserColumns._ID,
                    Contract.UserColumns.COLUMN_USER_VOORNAAM,
                    Contract.UserColumns.COLUMN_USER_NAAM,
                    Contract.UserColumns.COLUMN_USER_EMAIL,
                    Contract.UserColumns.COLUMN_USER_PHOTO };


        SQLiteDatabase db = DatabaseHelper.getINSTANCE(view.getContext()).getReadableDatabase();

        Cursor mData = db.query(Contract.UserDB.TABLE_NAME, projection, null, null, null, null, null);

        mData.moveToFirst();

        int colnr1 = mData.getColumnIndex(Contract.UserColumns.COLUMN_USER_VOORNAAM);
        int colnr2 = mData.getColumnIndex(Contract.UserColumns.COLUMN_USER_NAAM);
        int colnr3 = mData.getColumnIndex(Contract.UserColumns.COLUMN_USER_EMAIL);
        int colnr4 = mData.getColumnIndex(Contract.UserColumns.COLUMN_USER_PHOTO);

        txtUserNaam.setText(mData.getString(colnr1) + " " + mData.getString(colnr2));
        txtUserMail.setText(mData.getString(colnr3));
        Picasso.with(view.getContext()).load(mData.getString(colnr4)).transform(new CircleTransform()).into(imgUser);




    }





}

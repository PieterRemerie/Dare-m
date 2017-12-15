package be.nmct.howest.darem.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import be.nmct.howest.darem.provider.*;

/**
 * Created by katri on 11/12/2017.
 */

public class SaveNewFriendToDBTask  extends AsyncTask<ContentValues, Void, Void> {

    private Context mContext;

    public SaveNewFriendToDBTask(Context context) {

        mContext = context;

    }


    @Override
    protected Void doInBackground(ContentValues... values) {

        /*long insertId = DatabaseHelper.getINSTANCE(mContext).getWritableDatabase().insert(
                Contract.FriendsDB.TABLE_NAME, null, values[0]);*/
        Uri newUri = mContext.getContentResolver().insert(be.nmct.howest.darem.provider.Contract.FRIENDS_URI, values[0]);
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute((aVoid));
    }

}

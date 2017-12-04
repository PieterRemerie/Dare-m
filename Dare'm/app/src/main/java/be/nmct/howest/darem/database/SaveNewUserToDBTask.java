package be.nmct.howest.darem.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by katri on 4/12/2017.
 */

public class SaveNewUserToDBTask  extends AsyncTask<ContentValues, Void, Void>{

    private Context mContext;

    public SaveNewUserToDBTask(Context context) {

        mContext = context;

    }


    @Override
    protected Void doInBackground(ContentValues... values) {

        long insertId = DatabaseHelper.getINSTANCE(mContext).getWritableDatabase().insert(
                Contract.UserDB.TABLE_NAME, null, values[0]);
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute((aVoid));
    }
}

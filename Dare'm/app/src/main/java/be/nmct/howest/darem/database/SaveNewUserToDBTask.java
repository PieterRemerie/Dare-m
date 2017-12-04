package be.nmct.howest.darem.database;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Piete_000 on 28/11/2017.
 */

public class SaveNewUserToDBTask extends AsyncTask<ContentValues, Void, Void> {

    private Context mContext;

    public SaveNewUserToDBTask(Context context){
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(ContentValues... params) {
        long insertId = DatabaseHelper.getINSTANCE(mContext).getWritableDatabase().insert(Contract.UserDB.TABLE_NAME, null, params[0]);
        return (null);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

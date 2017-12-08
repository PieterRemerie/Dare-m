package be.nmct.howest.darem.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import be.nmct.howest.darem.provider.*;

/**
 * Created by Piete_000 on 29/10/2017.
 */

public class SaveNewChallengeToDBTask extends AsyncTask<ContentValues, Void, Void> {

    private Context mContext;

    public SaveNewChallengeToDBTask(Context context) {
        mContext = context;
    }


    protected Void doInBackground(ContentValues... values){
        /*long insertId = DatabaseHelper.getINSTANCE(mContext).getWritableDatabase().insert(
                Contract.ChallengesDB.TABLE_NAME, null, values[0]);*/
        Uri newUri = mContext.getContentResolver().insert(be.nmct.howest.darem.provider.Contract.CHALLENGES_URI, values[0]);
        return (null);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

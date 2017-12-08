package be.nmct.howest.darem.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.Console;

/**
 * Created by Piete_000 on 29/10/2017.
 */

public class ChallengesLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;
    private Context mContext;
    public ChallengesLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }
        if(takeContentChanged() || mData == null){
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        String[] columns = new String[]{
                Contract.ChallengesColumns._ID,
                Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATOR,
                Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM,
                Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION
        };
        mData = getContext().getContentResolver().query(be.nmct.howest.darem.provider.Contract.CHALLENGES_URI, columns, null, null, null);
        /*DatabaseHelper helper = DatabaseHelper.getINSTANCE(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        mData = db.query(Contract.ChallengesDB.TABLE_NAME,
                new String[]{
                        Contract.ChallengesColumns._ID,
                        Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM,
                        Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION,
                },
                null,
                null,
                null,
                null,
                null
                );*/
        mData.getCount();
        return mData;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if(isReset()){
            if(cursor != null){
                cursor.close();
            }
            return;
        }
        Cursor oldData = mData;
        mData = cursor;
        if(isStarted()){
            super.deliverResult(cursor);
        }
        if(oldData != null && oldData != cursor && !oldData.isClosed()){
            oldData.close();
        }

    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
    }
}

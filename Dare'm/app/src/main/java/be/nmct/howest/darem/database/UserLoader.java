package be.nmct.howest.darem.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by katri on 4/12/2017.
 */

public class UserLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;
    private Context mContext;

    public UserLoader(Context context) {
        super(context);
        mContext = context;
    }

    protected  void onStartLoading() {
        if(mData != null) {
            deliverResult(mData);
        }
        if(takeContentChanged() || mData == null){
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseHelper helper = DatabaseHelper.getINSTANCE(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        mData = db.query(Contract.UserDB.TABLE_NAME,
                new String[]{
                        Contract.UserColumns._ID,
                        Contract.UserColumns.COLUMN_USER_VOORNAAM,
                        Contract.UserColumns.COLUMN_USER_NAAM,
                        Contract.UserColumns.COLUMN_USER_EMAIL,
                        Contract.UserColumns.COLUMN_USER_PHOTO
                },
                null,
                null,
                null,
                null,
                null
                );
        mData.getCount();
        return mData;

    }

    public void deliverResult(Cursor cursor){
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

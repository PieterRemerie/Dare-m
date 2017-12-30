package be.nmct.howest.darem.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by katri on 15/12/2017.
 */

public class FriendLoader extends AsyncTaskLoader<Cursor>{

    private Cursor mData;

    public FriendLoader(Context context){
        super(context);
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
        mData = db.query(Contract.FriendsDB.TABLE_NAME,
                new String[]{
                        Contract.FriendsColumns._ID,
                        Contract.FriendsColumns.COLUMN_FRIEND_FULLNAME,
                        Contract.FriendsColumns.COLUMN_FRIEND_PHOTO
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

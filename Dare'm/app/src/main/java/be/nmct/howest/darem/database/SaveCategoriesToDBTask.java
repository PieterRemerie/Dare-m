package be.nmct.howest.darem.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import be.nmct.howest.darem.provider.*;
import be.nmct.howest.darem.provider.Contract;

/**
 * Created by katri on 18/12/2017.
 */

public class SaveCategoriesToDBTask extends AsyncTask<ContentValues, Void, Void>{



    private Context mContext;

    public SaveCategoriesToDBTask(Context context){
        mContext = context;
    }


    @Override
    protected Void doInBackground(ContentValues... contentValues) {

        Uri newUri = mContext.getContentResolver().insert(Contract.CATEGORIES_URI, contentValues[0]);

        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute((aVoid));
    }
}

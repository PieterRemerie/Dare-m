package be.nmct.howest.darem.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Piete_000 on 29/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper INSTANCE;
    private static Object object = new Object();
    public DatabaseHelper(Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
    }

    public static DatabaseHelper getINSTANCE(Context context) {
        if(INSTANCE == null){
            synchronized (object){
                INSTANCE = new DatabaseHelper(context.getApplicationContext());
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.ChallengesDB.CREATE_TABLE);
        db.execSQL(Contract.UserDB.CREATE_TABLE);
        db.execSQL(Contract.UserChallengeColumns.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contract.ChallengesDB.DELETE_TABLE);
        db.execSQL(Contract.UserDB.DELETE_TABLE);
        db.execSQL(Contract.UserChallengeColumns.DELETE_TABLE);
        onCreate(db);
    }
}

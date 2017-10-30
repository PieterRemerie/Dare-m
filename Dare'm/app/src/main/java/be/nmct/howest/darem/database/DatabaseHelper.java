package be.nmct.howest.darem.database;

import android.content.Context;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contract.ChallengesDB.DELETE_TABLE);
        onCreate(db);
    }

    /*public boolean InsertChallenge(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM, "hier komt model + get");
        contentValues.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION, "hier komt model + get");
        contentValues.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_CATEGORYID, "hier komt model + get");
        contentValues.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATORID, "hier komt model + get");
        contentValues.put(Contract.ChallengesColumns.COLUMN_CHALLENGE_ISCOMPLETED, "hier komt model + get");
        long result = db.insert(Contract.ChallengesDB.TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllChallenges(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Contract.ChallengesColumns.TABLE_NAME, null);
        return res;
    }*/
}

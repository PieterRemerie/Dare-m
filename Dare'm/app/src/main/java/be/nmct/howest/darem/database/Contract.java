package be.nmct.howest.darem.database;

import android.provider.BaseColumns;

/**
 * Created by Piete_000 on 29/10/2017.
 */

public class Contract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database.db";
    public interface ChallengesColumns extends BaseColumns{
        public static final String TABLE_NAME = "challenges";
        public static final String COLUMN_CHALLENGE_NAAM = "challengeNaam";
        public static final String COLUMN_CHALLENGE_DESCRIPTION = "challengeDescription";
    }
    public static abstract class ChallengesDB implements ChallengesColumns{
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_CHALLENGE_NAAM + " text not null, "
                + COLUMN_CHALLENGE_DESCRIPTION + " text not null "
                + ");";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /*public interface UserColumns extends BaseColumns{
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USER_NAAM = "userNaam";
        public static final String COLUMN_USER_VOORNAAM = "userVoornaam";
        public static final String COLUMN_USER_EMAIL= "userEmail";
    }
    public static abstract class UserDB implements UserColumns{
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + "integer primary key autoincrement"
                + COLUMN_USER_NAAM + " text not null, "
                + COLUMN_USER_VOORNAAM + " text not null, "
                + COLUMN_USER_EMAIL + " text not null "
                + ");";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }*/
}

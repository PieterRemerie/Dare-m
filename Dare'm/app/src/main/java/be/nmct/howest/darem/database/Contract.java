package be.nmct.howest.darem.database;

import android.os.ParcelUuid;
import android.provider.BaseColumns;

/**
 * Created by Piete_000 on 29/10/2017.
 */

public class Contract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database.db";

    public interface ChallengesColumns extends BaseColumns {
        public static final String TABLE_NAME = "challenges";
        public static final String COLUMN_CHALLENGE_NAAM = "challengeNaam";
        public static final String COLUMN_CHALLENGE_DESCRIPTION = "challengeDescription";
        public static final String COLUMN_CHALLENGE_USERS = "challengeFriends";
        public static final String COLUMN_CHALLENGE_CREATOR = "challengeCreator";
        public static final String COLUMN_CHALLENGE_DB = "challengeDB";
        public static final String COLUMN_CHALLENGE_CATEGORY ="challengeCategory";
        public static final String COLUMN_CHALLENGE_DATE = "challengeDate";

    }

    public static abstract class ChallengesDB implements ChallengesColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_CHALLENGE_CREATOR + " integer not null, "
                + COLUMN_CHALLENGE_NAAM + " text not null, "
                + COLUMN_CHALLENGE_DESCRIPTION + " text not null, "
                + COLUMN_CHALLENGE_DB + " text not null, "
                + COLUMN_CHALLENGE_CATEGORY + " text not null, "
                + COLUMN_CHALLENGE_DATE + " text"
                + ");";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface UserColumns extends BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USER_NAAM = "givenName";
        public static final String COLUMN_USER_VOORNAAM = "familyName";
        public static final String COLUMN_USER_EMAIL = "email";
        public static final String COLUMN_USER_POINTS = "points";
        public static final String COLUMN_USER_PHOTO = "photo";
    }

    public static abstract class UserDB implements UserColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_USER_NAAM + " text not null, "
                + COLUMN_USER_VOORNAAM + " text not null, "
                + COLUMN_USER_EMAIL + " text not null UNIQUE, "
                + COLUMN_USER_POINTS + " integer, "
                + COLUMN_USER_PHOTO + " text not null"
                + ");";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface UserChallengeColumns extends BaseColumns {
        public static final String TABLE_NAME = "UserChallenge";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_CHALLENGEID = "challengeID";

        public static abstract class UserChallengedB implements UserChallengeColumns {

        }

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_USERID + " integer not null, "
                + COLUMN_CHALLENGEID + " integer not null, "
                + "Foreign key (" + COLUMN_USERID + ") references "
                + UserColumns.TABLE_NAME + " (" + UserColumns._ID + ") on delete cascade, "
                + "Foreign key (" + COLUMN_CHALLENGEID + ") references "
                + ChallengesColumns.TABLE_NAME + " (" + ChallengesColumns._ID + ") on delete cascade"
                + ");";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface FriendsColumns extends BaseColumns {
        public static final String TABLE_NAME = "Friends";
        public static final String COLUMN_FRIEND_FULLNAME = "fullName";
        public static final String COLUMN_FRIEND_PHOTO = "photo";
    }

    public static abstract class FriendsDB implements FriendsColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_FRIEND_FULLNAME + " text not null, "
                + COLUMN_FRIEND_PHOTO + " text not null"
                +  ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface CategoryColumns extends BaseColumns {
        public static final String TABLE_NAME = "Categories";
        public static final String COLUMN_CATEGORY_NAME = "categorie";
        public static final String COLUMN_CATEGORY_IMG = "img";
    }

    public static abstract class CategoryDB implements CategoryColumns{
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_CATEGORY_NAME + " text not null, "
                + COLUMN_CATEGORY_IMG + " text not null"
                +  ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }

}

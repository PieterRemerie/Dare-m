package be.nmct.howest.darem.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import be.nmct.howest.darem.database.DatabaseHelper;

/**
 * Created by Piete_000 on 7/12/2017.
 */

public class ContentProviderDarem extends ContentProvider {
    private DatabaseHelper databaseHelper;

    private static final int CHALLENGES = 1;
    private static final int CHALLENGES_ID = 2;
    private static final int USERS = 3;
    private static final int USERS_ID = 4;
    private static final int FRIENDS = 5;
    private static final int FRIENDS_ID = 6;

    private static final UriMatcher uriMatcher;
    private static HashMap<String, String> DAREM_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Challenges
        uriMatcher.addURI(Contract.AUTHORITY, "challenges", CHALLENGES);
        uriMatcher.addURI(Contract.AUTHORITY, "challenges/#", CHALLENGES_ID);

        //Users
        uriMatcher.addURI(Contract.AUTHORITY, "users", USERS);
        uriMatcher.addURI(Contract.AUTHORITY, "users/#", USERS_ID);

        //Friends
        uriMatcher.addURI(Contract.AUTHORITY, "friends", FRIENDS);
        uriMatcher.addURI(Contract.AUTHORITY, "friends/#", FRIENDS_ID);
    }



    @Override
    public boolean onCreate() {
        databaseHelper = DatabaseHelper.getINSTANCE(getContext());
        DAREM_PROJECTION_MAP = new HashMap<>();

        //inladen Challenges
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns._ID, be.nmct.howest.darem.database.Contract.ChallengesColumns._ID);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATOR, be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATOR);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM, be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION, be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION);

        //inladen Users
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.UserColumns._ID, be.nmct.howest.darem.database.Contract.UserColumns._ID);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_VOORNAAM, be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_VOORNAAM);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_NAAM, be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_NAAM);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_EMAIL, be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_EMAIL);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_PHOTO, be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_PHOTO);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_POINTS, be.nmct.howest.darem.database.Contract.UserColumns.COLUMN_USER_POINTS);

        //inladen Friends
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.FriendsColumns._ID, be.nmct.howest.darem.database.Contract.FriendsColumns._ID);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.FriendsColumns.COLUMN_FRIEND_FULLNAME, be.nmct.howest.darem.database.Contract.FriendsColumns.COLUMN_FRIEND_FULLNAME);
        DAREM_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.FriendsColumns.COLUMN_FRIEND_PHOTO, be.nmct.howest.darem.database.Contract.FriendsColumns.COLUMN_FRIEND_PHOTO);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case CHALLENGES:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME);
                queryBuilder.setProjectionMap(DAREM_PROJECTION_MAP);
                break;

            case CHALLENGES_ID:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME);
                queryBuilder.setProjectionMap(DAREM_PROJECTION_MAP);

                /*String productid = uri.getPathSegments().get(Contract.CHALLENGE_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + be.nmct.howest.darem.database.Contract.ChallengesColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + productid});
*/
                break;
            case USERS:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.UserDB.TABLE_NAME);
                queryBuilder.setProjectionMap(DAREM_PROJECTION_MAP);
                break;
            case USERS_ID:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.UserDB.TABLE_NAME);
                queryBuilder.setProjectionMap(DAREM_PROJECTION_MAP);

                String productid = uri.getPathSegments().get(Contract.USER_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + be.nmct.howest.darem.database.Contract.UserColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + productid});

                break;
            case FRIENDS:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.FriendsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(DAREM_PROJECTION_MAP);
                break;
            case FRIENDS_ID:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.FriendsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(DAREM_PROJECTION_MAP);

                String friendsid = uri.getPathSegments().get(Contract.FRIEND_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + be.nmct.howest.darem.database.Contract.FriendsColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + friendsid});

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor data = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        data.getCount();

        data.setNotificationUri(getContext().getContentResolver(), uri);
        return data;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case CHALLENGES:
                return Contract.CHALLENGE_CONTENT_TYPE;
            case CHALLENGES_ID:
                return Contract.CHALLENGE_ITEM_CONTENT_TYPE;
            case USERS:
                return Contract.USER_CONTENT_TYPE;
            case USERS_ID:
                return Contract.USER_ITEM_CONTENT_TYPE;
            case FRIENDS:
                return Contract.FRIEND_CONTENT_TYPE;
            case FRIENDS_ID:
                return Contract.FRIEND_ITEM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long newRowId;
        switch (uriMatcher.match(uri)) {
            case CHALLENGES:
                newRowId = db.insert(
                        be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME, null, values);
                if (newRowId > 0) {
                    Uri daremItemUri = ContentUris.withAppendedId(Contract.CHALLENGES_ITEM_URI, newRowId);
                    //eventuele observers verwittigen
                    getContext().getContentResolver().notifyChange(daremItemUri, null);
                    return daremItemUri;
                }

                break;
            case USERS:
                newRowId = db.insert(be.nmct.howest.darem.database.Contract.UserDB.TABLE_NAME, null, values);
                if(newRowId > 0){
                    Uri daremItemUri = ContentUris.withAppendedId(Contract.USERS_ITEM_URI, newRowId);
                    //eventuele observers verwittigen
                    getContext().getContentResolver().notifyChange(daremItemUri, null);
                    return daremItemUri;
                }
                break;
            case FRIENDS:
                newRowId = db.insert(be.nmct.howest.darem.database.Contract.FriendsDB.TABLE_NAME, null, values);
                if(newRowId > 0){
                    Uri daremItemUri = ContentUris.withAppendedId(Contract.FRIENDS_ITEM_URI, newRowId);
                    //eventuele observers verwittigen
                    getContext().getContentResolver().notifyChange(daremItemUri, null);
                    return daremItemUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        throw new IllegalArgumentException();
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String finalWhere;
        int count;
        switch (uriMatcher.match(uri)) {
            case CHALLENGES:
                count = db.delete(
                        be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case CHALLENGES_ID:
                String challengeId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + challengeId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;
            case USERS:
                count = db.delete(
                        be.nmct.howest.darem.database.Contract.UserDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case USERS_ID:
                String userId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + userId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        be.nmct.howest.darem.database.Contract.UserDB.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;
            case FRIENDS:
                count = db.delete(
                        be.nmct.howest.darem.database.Contract.FriendsDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case FRIENDS_ID:
                String friendId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + friendId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        be.nmct.howest.darem.database.Contract.FriendsDB.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (uriMatcher.match(uri)) {
            case CHALLENGES:
                count = db.update(
                        be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case CHALLENGES_ID:
                String challengeId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + challengeId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME,
                        values,
                        finalWhere,
                        selectionArgs
                );
                break;
            case USERS:
                count = db.update(
                        be.nmct.howest.darem.database.Contract.UserDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case USERS_ID:
                String userId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + userId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        be.nmct.howest.darem.database.Contract.UserDB.TABLE_NAME,
                        values,
                        finalWhere,
                        selectionArgs
                );
                break;
            case FRIENDS:
                count = db.update(
                        be.nmct.howest.darem.database.Contract.FriendsDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case FRIENDS_ID:
                String friendId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + friendId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        be.nmct.howest.darem.database.Contract.FriendsDB.TABLE_NAME,
                        values,
                        finalWhere,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}

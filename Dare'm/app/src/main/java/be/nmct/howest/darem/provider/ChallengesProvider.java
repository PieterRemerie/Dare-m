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
 * Created by Piete_000 on 5/12/2017.
 */

public class ChallengesProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    private static final int CHALLENGES = 1;
    private static final int CHALLENGES_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, "challenges", CHALLENGES);
        uriMatcher.addURI(Contract.AUTHORITY, "challenges/#", CHALLENGES_ID);
    }

    private static HashMap<String, String> CHALLENGES_PROJECTION_MAP;

    @Override
    public boolean onCreate() {
        databaseHelper = DatabaseHelper.getINSTANCE(getContext());
        CHALLENGES_PROJECTION_MAP = new HashMap<>();
        CHALLENGES_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns._ID, be.nmct.howest.darem.database.Contract.ChallengesColumns._ID);
        CHALLENGES_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATOR, be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_CREATOR);
        CHALLENGES_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM, be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_NAAM);
        CHALLENGES_PROJECTION_MAP.put(be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION, be.nmct.howest.darem.database.Contract.ChallengesColumns.COLUMN_CHALLENGE_DESCRIPTION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case CHALLENGES:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME);
                queryBuilder.setProjectionMap(CHALLENGES_PROJECTION_MAP);
                break;

            case CHALLENGES_ID:
                queryBuilder.setTables(be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME);
                queryBuilder.setProjectionMap(CHALLENGES_PROJECTION_MAP);

                String productid = uri.getPathSegments().get(Contract.CHALLENGE_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + be.nmct.howest.darem.database.Contract.ChallengesColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + productid});

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
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CHALLENGES:
                long newRowId = db.insert(
                        be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME, null, values);
                if (newRowId > 0) {
                    Uri productItemUri = ContentUris.withAppendedId(Contract.CHALLENGES_ITEM_URI, newRowId);
                    //eventuele observers verwittigen
                    getContext().getContentResolver().notifyChange(productItemUri, null);
                    return productItemUri;
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
                String productItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + productItemId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        be.nmct.howest.darem.database.Contract.ChallengesDB.TABLE_NAME,
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
                String productId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + productId;

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
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}

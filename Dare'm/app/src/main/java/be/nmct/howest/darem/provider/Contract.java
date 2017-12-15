package be.nmct.howest.darem.provider;

import android.content.ContentProvider;
import android.content.Context;
import android.net.Uri;

/**
 * Created by Piete_000 on 5/12/2017.
 */

public class Contract {

    public static final String AUTHORITY = "be.howest.nmct.darem";

    //CONTENT-URIS
    public static final Uri CHALLENGES_URI = Uri.parse("content://" + AUTHORITY + "/challenges");
    public static final Uri CHALLENGES_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/challenges/");

    public static final Uri USERS_URI = Uri.parse("content://" + AUTHORITY + "/users/");
    public static final Uri USERS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/users/");

    public static final Uri FRIENDS_URI = Uri.parse("content://" + AUTHORITY + "/friends/");
    public static final Uri FRIENDS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/friends/");

    //MIME-TYPES

    public static final String CHALLENGE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.challenge";
    public static final String CHALLENGE_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.howest.challenge";

    public static final String USER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.user";
    public static final String USER_ITEM_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.user";

    public static final String FRIEND_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.friend";
    public static final String FRIEND_ITEM_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.friend";

    public static final int CHALLENGE_ID_PATH_POSITION = 1;
    public static final int USER_ID_PATH_POSITION = 1;
    public static final int FRIEND_ID_PATH_POSITION= 1;

    public static void clearAllContent(Context context){
        context.getContentResolver().delete(Contract.CHALLENGES_URI, null, null);
        context.getContentResolver().delete(Contract.CHALLENGES_ITEM_URI, null, null);

        context.getContentResolver().delete(Contract.USERS_URI, null, null);
        context.getContentResolver().delete(Contract.USERS_ITEM_URI, null, null);

        context.getContentResolver().delete(Contract.FRIENDS_URI, null, null);
        context.getContentResolver().delete(Contract.FRIENDS_URI, null, null);

    }

}

package be.nmct.howest.darem.provider;

import android.net.Uri;

/**
 * Created by Piete_000 on 5/12/2017.
 */

public class Contract {

    public static final String AUTHORITY = "be.howest.nmct.darem";

    //CONTENT-URIS
    public static final Uri CHALLENGES_URI = Uri.parse("content://" + AUTHORITY + "/challenges");
    public static final Uri CHALLENGES_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/products/");

    //MIME-TYPES

    public static final String CHALLENGE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.challenge";
    public static final String CHALLENGE_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.howest.challenge";

    public static final int CHALLENGE_ID_PATH_POSITION = 1;

}

package be.nmct.howest.darem.Loader;

import android.provider.BaseColumns;

/**
 * Created by michv on 28/12/2017.
 */

public class ParticipantsContract {

    public interface Columns extends BaseColumns {

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_COMPLETED = "completed";

    }
}

package be.nmct.howest.darem.Loader;

import android.provider.BaseColumns;

/**
 * Created by michv on 14/12/2017.
 */

public class Challenge {
    public interface Columns extends BaseColumns {

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DATE = "date";
    }
}

package be.nmct.howest.darem.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import be.nmct.howest.darem.auth.AuthHelper;

/**
 * Created by michv on 29/12/2017.
 */

public class SyncManual {
    public static void syncDataManual(Context context) {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        if (AuthHelper.getAccount(context) != null) {
            context.getContentResolver().requestSync(AuthHelper.getAccount(context), be.nmct.howest.darem.provider.Contract.AUTHORITY, settingsBundle);
        }
    }
}

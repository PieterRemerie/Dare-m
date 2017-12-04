package be.nmct.howest.darem.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Piete_000 on 28/11/2017.
 */

public class AuthenticatorService extends Service{

    private Authenticator mAuthenticator;

    public AuthenticatorService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new Authenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

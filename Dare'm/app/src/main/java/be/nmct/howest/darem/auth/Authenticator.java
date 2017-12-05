package be.nmct.howest.darem.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import be.nmct.howest.darem.LoginActivity;

/**
 * Created by Piete_000 on 28/11/2017.
 */

public class Authenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    public Authenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        if(!accountType.equals(Contract.ACCOUNT_TYPE))
            throw new IllegalArgumentException();
        if(!(requiredFeatures == null || requiredFeatures.length == 0))
            throw new IllegalArgumentException();

        return createAuthenticatorActivityBundle(response);
    }

    private Bundle createAuthenticatorActivityBundle(AccountAuthenticatorResponse response){
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        if(authTokenType != "access_token") throw new IllegalArgumentException("Only access_token is available");

        AccountManager mgt = AccountManager.get(mContext);
        String accessToken = mgt.peekAuthToken(account, authTokenType);

        //geen access-token aanwezig --> doorverwijzen naar Login-Activity
        if (accessToken == null)
            return createAuthenticatorActivityBundle(response);

        //plaats accessToken in een bundle samen met name en type
        return createAccessTokenBundle(account, accessToken);
    }

    private Bundle createAccessTokenBundle(Account account, String accessToken) {
        Bundle reply = new Bundle();
        reply.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
        reply.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        reply.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        return reply;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if(authTokenType.equals("access_token")){
            return "Access Token";
        }else{
            return null;
        }
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}

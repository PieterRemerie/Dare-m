package be.nmct.howest.darem.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;

import java.io.IOException;

/**
 * Created by Michiel on 28/11/2017.
 */

public class AuthHelper {
    private static AccountManager mAccountManager;
    private static AccountAuthenticatorResponse mAccountAuthenticatorResponse;

    public static String getUsername(Context context){
        mAccountManager = AccountManager.get(context);

        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if(accounts.length>0){
            return accounts[0].name;
        }else{
            return null;
        }
    }

    public static Bundle getAuthToken(Context context){
        mAccountManager = AccountManager.get(context);
        Account[] acc = mAccountManager.getAccountsByType(be.nmct.howest.darem.auth.Contract.ACCOUNT_TYPE);


        AccountManagerFuture<Bundle> bundleToken = mAccountManager.getAuthToken(acc[0], "access_token", null, null, null, null);
        Bundle authToken = new Bundle();
        try {
            authToken = bundleToken.getResult();
            return authToken;
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }

        return authToken;
    }
}

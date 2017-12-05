package be.nmct.howest.darem.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Created by Piete_000 on 28/11/2017.
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
}

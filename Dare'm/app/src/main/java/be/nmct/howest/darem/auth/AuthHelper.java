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

    public static String getUsername(Context context){
        mAccountManager = AccountManager.get(context);

        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if(accounts.length>0){
            return accounts[0].name;
        }else{
            return null;
        }
    }

    public static Account getAccount(Context context){
        mAccountManager = AccountManager.get(context);

        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if(accounts.length>0){
            return accounts[0];
        }
        else return null;
    }

    public static String getAccessToken(Context context) {
        AccountManager mgt = AccountManager.get(context);
        String accessToken = mgt.peekAuthToken(getAccount(context), "access_token");
        return accessToken;
    }

    public static String getDbToken(Context context) {
        AccountManager mgt = AccountManager.get(context);
        String accessToken = mgt.peekAuthToken(getAccount(context), "db_token");
        return accessToken;
    }

    public static Boolean isUserLoggedIn(Context context){
        mAccountManager = AccountManager.get(context);
        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        if(accounts.length>0){
            return true;
        }
        else return false;

    }

    public static void logUserOff(Context context) {
        mAccountManager = AccountManager.get(context);
        Account[] accounts = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        for (int index = 0; index < accounts.length; index++) {
            mAccountManager.removeAccount(accounts[index], null, null, null);
        }
    }
 }

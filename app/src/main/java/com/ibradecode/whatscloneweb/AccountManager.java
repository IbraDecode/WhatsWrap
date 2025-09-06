package com.ibradecode.whatscloneweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.WebStorage;

import java.util.HashSet;
import java.util.Set;

public class AccountManager {

    private static final String PREFS_NAME = "WhatsCloneWebAccounts";
    private static final String KEY_ACTIVE_ACCOUNT = "active_account";
    private static final String KEY_ACCOUNT_LIST = "account_list";

    private Context context;
    private SharedPreferences sharedPreferences;

    public AccountManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveAccountData(String accountId) {
        // Save cookies
        String cookies = CookieManager.getInstance().getCookie("https://web.whatsapp.com/");
        sharedPreferences.edit().putString("cookies_" + accountId, cookies).apply();

        // Save local storage (more complex, requires JavaScript injection)
        // For now, we'll just save a placeholder. Actual local storage saving will be done via JS.
        // We'll need to inject JS to get localStorage data and pass it back to Android.
        // This is a simplified approach for demonstration.
        sharedPreferences.edit().putString("local_storage_" + accountId, "placeholder").apply();

        // Add account to list
        Set<String> accountList = getAccountList();
        accountList.add(accountId);
        sharedPreferences.edit().putStringSet(KEY_ACCOUNT_LIST, accountList).apply();
    }

    public void loadAccountData(String accountId) {
        // Load cookies
        String cookies = sharedPreferences.getString("cookies_" + accountId, null);
        if (cookies != null) {
            CookieManager.getInstance().setCookie("https://web.whatsapp.com/", cookies);
        }

        // Load local storage (requires JavaScript injection)
        // Actual local storage loading will be done via JS.
    }

    public void setActiveAccount(String accountId) {
        sharedPreferences.edit().putString(KEY_ACTIVE_ACCOUNT, accountId).apply();
    }

    public String getActiveAccount() {
        return sharedPreferences.getString(KEY_ACTIVE_ACCOUNT, null);
    }

    public Set<String> getAccountList() {
        return sharedPreferences.getStringSet(KEY_ACCOUNT_LIST, new HashSet<String>());
    }

    public void clearAccountData(String accountId) {
        sharedPreferences.edit().remove("cookies_" + accountId).apply();
        sharedPreferences.edit().remove("local_storage_" + accountId).apply();

        Set<String> accountList = getAccountList();
        accountList.remove(accountId);
        sharedPreferences.edit().putStringSet(KEY_ACCOUNT_LIST, accountList).apply();
    }

    public void clearAllWebViewData() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        WebStorage.getInstance().deleteAllData();
    }
}



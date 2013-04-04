package com.github.michaelengland.managers;

import android.accounts.Account;
import android.accounts.AccountManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginManager {
    private static final String SOUND_CLOUD_ACCOUNT_TYPE = "com.soundcloud.android.account";

    AccountManager accountManager;
    SettingsManager settingsManager;

    @Inject
    LoginManager(final AccountManager accountManager, final SettingsManager settingsManager) {
        this.accountManager = accountManager;
        this.settingsManager = settingsManager;
    }

    public boolean isLoggedIn() {
        return settingsManager.getToken() != null;
    }

    public Account soundCloudAccount() {
        Account[] accounts = accountManager.getAccountsByType(SOUND_CLOUD_ACCOUNT_TYPE);
        if (accounts.length > 0) {
            return accounts[0];
        } else {
            return null;
        }
    }

    public String suggestedUsername() {
        Account account = soundCloudAccount();
        if (account != null) {
            return account.name;
        } else {
            return null;
        }
    }
}

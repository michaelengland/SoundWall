package com.github.michaelengland.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import com.soundcloud.api.Token;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginManager {
    private static final String SOUND_CLOUD_ACCOUNT_TYPE = "com.soundcloud.android.account";

    private AccountManager accountManager;
    private SettingsManager settingsManager;
    private Bus bus;

    @Inject
    LoginManager(final AccountManager accountManager, final SettingsManager settingsManager, final Bus bus) {
        this.accountManager = accountManager;
        this.settingsManager = settingsManager;
        this.bus = bus;
        bus.register(this);
    }

    public boolean isLoggedIn() {
        return settingsManager.getToken() != null;
    }

    public void login(Token token) {
        setToken(token);
    }

    public void logout() {
        setToken(null);
    }

    private void setToken(Token token) {
        settingsManager.setToken(token);
        bus.post(new UserStateChangeEvent());
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

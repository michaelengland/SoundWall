package com.github.michaelengland.managers;

import android.content.SharedPreferences;
import com.soundcloud.api.Token;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsManager {
    private static final String TOKEN_ACCESS_KEY = "token_access";
    private static final String TOKEN_REFRESH_KEY = "token_refresh";
    private static final String TOKEN_SCOPE_KEY = "token_scope";

    private SharedPreferences sharedPreferences;

    private Token token;

    @Inject
    SettingsManager(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public synchronized Token getToken() {
        if (token == null) {
            initializeTokenFromSharedPreferences();
        }
        return token;
    }

    private void initializeTokenFromSharedPreferences() {
        String access = sharedPreferences.getString(TOKEN_ACCESS_KEY, null);
        String refresh = sharedPreferences.getString(TOKEN_REFRESH_KEY, null);
        String scope = sharedPreferences.getString(TOKEN_SCOPE_KEY, null);
        if (access != null) {
            token = new Token(access, refresh, scope);
        }
    }

    public synchronized void setToken(Token token) {
        this.token = token;
        saveTokenToSharedPreferences();
    }

    private void saveTokenToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (token != null) {
            editor.putString(TOKEN_ACCESS_KEY, token.access);
            editor.putString(TOKEN_REFRESH_KEY, token.refresh);
            editor.putString(TOKEN_SCOPE_KEY, token.scope);
        } else {
            editor.remove(TOKEN_ACCESS_KEY);
            editor.remove(TOKEN_REFRESH_KEY);
            editor.remove(TOKEN_SCOPE_KEY);
        }
        editor.commit();
    }
}

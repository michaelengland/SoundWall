package com.github.michaelengland.api;

import android.os.AsyncTask;
import com.soundcloud.api.Token;

import javax.inject.Inject;

public class LoginTask extends AsyncTask<Void, Void, Token> {
    private String username, password;

    private LoginTaskListener listener;

    @Inject
    SoundCloudClient client;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setListener(LoginTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Token doInBackground(Void... voids) {
        try {
            return client.login(username, password);
        } catch (SoundCloudClientException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Token token) {
        if (listener != null) {
            if (token != null) {
                listener.onLoginSuccess();
            } else {
                listener.onLoginFailure();
            }
        }
    }

    public static interface LoginTaskListener {
        void onLoginSuccess();

        void onLoginFailure();
    }
}

package com.github.michaelengland.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import com.github.michaelengland.R;
import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.fragments.LoginDialogFragment;
import com.github.michaelengland.fragments.SettingsFragment;
import com.github.michaelengland.managers.LoginManager;

import javax.inject.Inject;

public class SettingsActivity extends PreferenceActivity implements SettingsFragment.OnLoginAttemptRequestedListener,
        LoginDialogFragment.OnLoginInputListener {
    private static final String TAG = "SettingsActivity";

    private static final String LOGIN_DIALOG_TAG = "LoginDialog";

    private static final String USERNAME_KEY = "Username";
    private static final String PASSWORD_KEY = "Password";

    @Inject
    LoginManager loginManager;
    String username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        SoundWallApplication.getInstance().inject(this);
        setContentView(R.layout.settings);
        if (savedInstanceState != null) {
            restoreLoginDetails(savedInstanceState);
        } else {
            setDefaultLoginDetails();
        }
    }

    private void restoreLoginDetails(Bundle state) {
        username = state.getString(USERNAME_KEY);
        password = state.getString(PASSWORD_KEY);
    }

    private void setDefaultLoginDetails() {
        username = loginManager.suggestedUsername();
        password = "";
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        saveLoginDetails(outState);
    }

    private void saveLoginDetails(Bundle outState) {
        outState.putString(USERNAME_KEY, username);
        outState.putString(PASSWORD_KEY, password);
    }

    @Override
    public void onLoginAttemptRequested() {
        LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance(username, password);
        loginDialogFragment.show(getFragmentManager(), LOGIN_DIALOG_TAG);
    }

    @Override
    public void onLoginInputted(String username, String password) {
        Log.d(TAG, "USER LOGIN: " + username);
    }

    @Override
    public void onLoginCancelled() {
        // NO-OP
    }
}

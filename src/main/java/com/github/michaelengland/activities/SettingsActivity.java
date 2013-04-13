package com.github.michaelengland.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;
import com.github.michaelengland.R;
import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.fragments.LoginInputFragment;
import com.github.michaelengland.fragments.LoginTaskFragment;
import com.github.michaelengland.fragments.SettingsFragment;
import com.github.michaelengland.managers.LoginManager;

import javax.inject.Inject;

public class SettingsActivity extends PreferenceActivity implements SettingsFragment.OnLoginAttemptRequestedListener,
        LoginInputFragment.OnLoginInputListener, LoginTaskFragment.OnLoginStatusChangedListener {
    private static final String TAG = "SettingsActivity";

    private static final String LOGIN_INPUT_TAG = "LoginInput";
    private static final String LOGIN_ACTION_TAG = "LoadingAction";

    private static final String USERNAME_KEY = "Username";
    private static final String PASSWORD_KEY = "Password";

    @Inject
    LoginManager loginManager;

    private LoginTaskFragment loginTaskFragment;

    private String username, password;

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
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        if (loginTaskFragment != null && loginTaskFragment.isVisible()) {
            loginTaskFragment.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onLoginAttemptRequested() {
        showLoginInputDialog();
    }

    private void showLoginInputDialog() {
        LoginInputFragment loginInputFragment = LoginInputFragment.newInstance(username, password);
        loginInputFragment.show(getFragmentManager(), LOGIN_INPUT_TAG);
    }

    @Override
    public void onLoginInputted(String username, String password) {
        this.username = username;
        this.password = password;
        performLogin();
    }

    private void performLogin() {
        loginTaskFragment = LoginTaskFragment.newInstance(username, password);
        loginTaskFragment.show(getFragmentManager(), LOGIN_ACTION_TAG);
    }

    @Override
    public void onLoginSuccessful() {
        loginTaskFragment.dismiss();
        Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailed() {
        this.password = "";
        loginTaskFragment.dismiss();
        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
        showLoginInputDialog();
    }
}

package com.github.michaelengland.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import com.github.michaelengland.R;
import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.managers.LoginManager;
import com.github.michaelengland.managers.UserStateChangeEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";

    private static final Uri SOUND_WALL_URI = Uri.parse("https://github.com/michaelengland/SoundWall");
    private static final Uri SOUND_CLOUD_URI = Uri.parse("https://soundcloud.com");

    OnLoginAttemptRequestedListener listener;

    @Inject
    LoginManager loginManager;
    @Inject
    Bus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        SoundWallApplication.getInstance().inject(this);
        addPreferencesFromResource(R.xml.settings);
        setupLoginPreference();
        setupUriPreferences();
        bus.register(this);
    }

    private void setupLoginPreference() {
        Preference loginPreference = getPreferenceWithKeyId(R.string.login_key);
        if (loginManager.isLoggedIn()) {
            loginPreference.setTitle(R.string.logout);
            loginPreference.setOnPreferenceClickListener(new LogoutPreferenceClickListener(this));
        } else {
            loginPreference.setTitle(R.string.login);
            loginPreference.setOnPreferenceClickListener(new LoginPreferenceClickListener(this));
        }
    }

    private void setupUriPreferences() {
        getPreferenceWithKeyId(R.string.sound_wall_key).setOnPreferenceClickListener(
                new UriOpenerPreferenceClickListener(this, SOUND_WALL_URI));
        getPreferenceWithKeyId(R.string.sound_cloud_key).setOnPreferenceClickListener(
                new UriOpenerPreferenceClickListener(this, SOUND_CLOUD_URI));
    }

    private Preference getPreferenceWithKeyId(int resId) {
        return getPreferenceManager().findPreference(getString(resId));
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        if (activity instanceof OnLoginAttemptRequestedListener) {
            listener = (OnLoginAttemptRequestedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement SettingsFragment.OnLoginPreferenceClickedListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
        listener = null;
        bus.unregister(this);
    }

    @Subscribe
    public void onUserStateChange(UserStateChangeEvent event) {
        setupLoginPreference();
    }

    public static interface OnLoginAttemptRequestedListener {
        void onLoginAttemptRequested();

        void onLogoutRequested();
    }

    static class LoginPreferenceClickListener implements Preference.OnPreferenceClickListener {
        private SettingsFragment fragment;

        LoginPreferenceClickListener(final SettingsFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            fragment.listener.onLoginAttemptRequested();
            return true;
        }
    }

    static class LogoutPreferenceClickListener implements Preference.OnPreferenceClickListener {
        private SettingsFragment fragment;

        LogoutPreferenceClickListener(final SettingsFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            fragment.listener.onLogoutRequested();
            return true;
        }
    }

    static class UriOpenerPreferenceClickListener implements Preference.OnPreferenceClickListener {
        private Uri uri;
        private SettingsFragment fragment;

        UriOpenerPreferenceClickListener(final SettingsFragment fragment, final Uri uri) {
            this.fragment = fragment;
            this.uri = uri;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            fragment.startActivity(intentForUri());
            return true;
        }

        private Intent intentForUri() {
            return new Intent(Intent.ACTION_VIEW, uri);
        }
    }
}

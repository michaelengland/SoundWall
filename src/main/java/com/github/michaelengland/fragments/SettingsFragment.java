package com.github.michaelengland.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import com.github.michaelengland.R;

public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";

    private static final Uri SOUND_WALL_URI = Uri.parse("https://github.com/michaelengland/SoundWall");
    private static final Uri SOUND_CLOUD_URI = Uri.parse("https://soundcloud.com");

    OnLoginAttemptRequestedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setPreferenceClickListeners();
    }

    private void setPreferenceClickListeners() {
        getPreferenceWithKeyId(R.string.login_key).setOnPreferenceClickListener(
                new LoginPreferenceClickListener(this));
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
    }

    public static interface OnLoginAttemptRequestedListener {
        public void onLoginAttemptRequested();
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

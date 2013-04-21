package com.github.michaelengland.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.github.michaelengland.R;
import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.api.LoginTask;
import com.soundcloud.api.Token;

import javax.inject.Inject;
import javax.inject.Provider;

public class LoginTaskFragment extends DialogFragment implements LoginTask.LoginTaskListener {
    private static final String TAG = "LoginTaskFragment";

    private static final String USERNAME_KEY = "Username";
    private static final String PASSWORD_KEY = "Password";

    private View loadingView;

    @Inject
    Provider<LoginTask> loginTaskProvider;
    private LoginTask currentLoginTask;

    private OnLoginStatusChangedListener listener;

    public static LoginTaskFragment newInstance(String username, String password) {
        LoginTaskFragment loginTaskFragment = new LoginTaskFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);
        args.putString(PASSWORD_KEY, password);
        loginTaskFragment.setArguments(args);
        return loginTaskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        SoundWallApplication.getInstance().inject(this);
        setRetainInstance(true);
        setupLoadingView();
        startLoginTask();
    }

    private void setupLoadingView() {
        loadingView = getActivity().getLayoutInflater().inflate(R.layout.loading, null);
    }

    private void startLoginTask() {
        Bundle args = getArguments();
        currentLoginTask = loginTaskProvider.get();
        currentLoginTask.setUsername(args.getString(USERNAME_KEY));
        currentLoginTask.setPassword(args.getString(PASSWORD_KEY));
        currentLoginTask.setListener(this);
        currentLoginTask.execute();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.loading)
                .setView(loadingView)
                .setCancelable(false)
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnLoginStatusChangedListener) {
            this.listener = (OnLoginStatusChangedListener) activity;
        } else {
            throw new IllegalArgumentException(activity.toString()
                    + " must implement LoginTaskFragment.OnLoginStatusChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onLoginSuccess(final Token token) {
        if (listener != null) {
            listener.onLoginSuccessful(token);
        }
    }

    @Override
    public void onLoginFailure() {
        if (listener != null) {
            listener.onLoginFailed();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        currentLoginTask.cancel(true);
    }

    public static interface OnLoginStatusChangedListener {
        void onLoginSuccessful(final Token token);

        void onLoginFailed();
    }
}

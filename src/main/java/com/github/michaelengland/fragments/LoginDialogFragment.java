package com.github.michaelengland.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.Views;
import com.github.michaelengland.R;

public class LoginDialogFragment extends DialogFragment {
    private static final String TAG = "LoginDialogFragment";

    private static final String USERNAME_KEY = "Username";
    private static final String PASSWORD_KEY = "Password";

    OnLoginInputListener listener;

    View loginView;

    @InjectView(R.id.login_text)
    EditText loginText;
    @InjectView(R.id.password_text)
    EditText passwordText;

    public static LoginDialogFragment newInstance(String username, String password) {
        LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);
        args.putString(PASSWORD_KEY, password);
        loginDialogFragment.setArguments(args);
        return loginDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setupLoginView();
        if (savedInstanceState != null) {
            setLoginState(savedInstanceState);
        } else {
            setLoginState(getArguments());
        }
    }

    private void setupLoginView() {
        loginView = getActivity().getLayoutInflater().inflate(R.layout.login, null);
        Views.inject(this, loginView);
    }

    private void setLoginState(Bundle state) {
        loginText.setText(state.getString(USERNAME_KEY));
        passwordText.setText(state.getString(PASSWORD_KEY));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.login)
                .setView(loginView)
                .setPositiveButton(R.string.login, new LoginClickListener(this))
                .setNegativeButton(android.R.string.cancel, new CancelClickListener(this))
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        if (activity instanceof OnLoginInputListener) {
            listener = (OnLoginInputListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginDialogFragment.OnLoginInputListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
        listener = null;
    }

    public static interface OnLoginInputListener {
        public void onLoginInputted(String username, String password);

        public void onLoginCancelled();
    }

    static class LoginClickListener implements DialogInterface.OnClickListener {
        LoginDialogFragment fragment;

        LoginClickListener(LoginDialogFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            fragment.listener.onLoginInputted(fragment.loginText.getText().toString(),
                    fragment.passwordText.getText().toString());
        }
    }

    static class CancelClickListener implements DialogInterface.OnClickListener {
        LoginDialogFragment fragment;

        CancelClickListener(LoginDialogFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            fragment.listener.onLoginCancelled();
        }
    }
}

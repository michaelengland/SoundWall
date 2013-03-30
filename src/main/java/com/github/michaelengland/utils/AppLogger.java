package com.github.michaelengland.utils;

import android.util.Log;

public class AppLogger {
    private static final String TAG = "SoundWall";

    public void log(String message) {
        Log.d(TAG, message);
    }
}

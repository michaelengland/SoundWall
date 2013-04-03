package com.github.michaelengland.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.github.michaelengland.R;
import com.github.michaelengland.SoundWallApplication;

public class HelloAndroidActivity extends Activity {
    private static final String TAG = "HelloAndroid";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoundWallApplication.getInstance().inject(this);
        Log.d(TAG, "Hello Activity Created");
        setContentView(R.layout.main);
    }
}

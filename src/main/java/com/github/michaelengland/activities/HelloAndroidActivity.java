package com.github.michaelengland.activities;

import android.app.Activity;
import android.os.Bundle;
import com.github.michaelengland.R;

public class HelloAndroidActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
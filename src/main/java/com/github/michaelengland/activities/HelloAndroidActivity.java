package com.github.michaelengland.activities;

import android.app.Activity;
import android.os.Bundle;
import com.github.michaelengland.R;
import com.github.michaelengland.utils.AppLogger;

import javax.inject.Inject;

public class HelloAndroidActivity extends Activity {
    @Inject
    AppLogger logger;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.log("Hello Activity Created");
        setContentView(R.layout.main);
    }
}

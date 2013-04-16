package com.github.michaelengland.wallpaper;

import android.view.GestureDetector;
import android.view.MotionEvent;

// Adapted from http://stackoverflow.com/questions/937313/android-basic-gesture-detection
public abstract class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_MAX_OFF_PATH = 200;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 500;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
            return false;
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            onSwipeLeft();
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            onSwipeRight();
        }
        return false;
    }

    public abstract void onSwipeLeft();

    public abstract void onSwipeRight();
}

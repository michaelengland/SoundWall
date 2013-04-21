package com.github.michaelengland.services;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.wallpaper.FlingGestureListener;
import com.github.michaelengland.wallpaper.SoundWallArtist;
import com.github.michaelengland.wallpaper.WallpaperState;
import com.github.michaelengland.wallpaper.WallpaperStateController;

import javax.inject.Inject;
import javax.inject.Provider;

public class SoundWallWallpaperService extends WallpaperService {
    private static final String TAG = "SoundWallWallpaperService";
    private static final int FRAME_RATE = 50;

    @Inject
    Provider<SoundWallArtist> artistProvider;
    @Inject
    Provider<WallpaperStateController> controllerProvider;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
        SoundWallApplication.getInstance().inject(this);
    }

    @Override
    public Engine onCreateEngine() {
        Log.d(TAG, "onCreateEngine()");
        return new SoundWallWallpaperServiceEngine();
    }

    class SoundWallWallpaperServiceEngine extends WallpaperService.Engine implements WallpaperStateController
            .WallpaperStateChangeListener {
        private static final String TAG = "SoundWallWallpaperServiceEngine";

        private volatile WallpaperState state;
        volatile boolean visible;

        private WallpaperStateController wallpaperStateController;
        private GestureDetector gestureDetector;
        private SoundWallArtist artist;
        /**
         * Package scope for tests
         */
        Handler wallpaperDrawingHandler;
        /**
         * Package scope for tests
         */
        Runnable wallpaperDrawer;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            Log.d(TAG, "onCreate()");
            super.onCreate(surfaceHolder);
            wallpaperDrawingHandler = new Handler();
            wallpaperDrawer = new SoundWallWallpaperDrawer(this);
            wallpaperStateController = controllerProvider.get();
            wallpaperStateController.setListener(this);
            state = wallpaperStateController.getState();
            wallpaperStateController.start();
            artist = artistProvider.get();
            artist.setContext(getBaseContext());
            gestureDetector = new GestureDetector(getBaseContext(), new TrackGestureListener(this));
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.d(TAG, "onVisibilityChanged()");
            super.onVisibilityChanged(visible);
            this.visible = visible;
            if (visible) {
                draw();
            } else {
                wallpaperDrawingHandler.removeCallbacks(wallpaperDrawer);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            Log.d(TAG, "onTouchEvent()");
            super.onTouchEvent(event);
            gestureDetector.onTouchEvent(event);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "onSurfaceChanged()");
            super.onSurfaceChanged(holder, format, width, height);
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "onSurfaceDestroyed()");
            super.onSurfaceDestroyed(holder);
            visible = false;
            stopDrawing();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            Log.d(TAG, "onOffsetsChanged()");
            super.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels, yPixels);
            draw();
        }

        @Override
        public void onDestroy() {
            Log.d(TAG, "onDestroy()");
            super.onDestroy();
            artist.setContext(null);
            wallpaperStateController.stop();
        }

        void draw() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    artist.draw(state, canvas);
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas);
            }

            stopDrawing();
            if (visible) {
                startDrawing();
            }
        }

        private void startDrawing() {
            wallpaperDrawingHandler.postDelayed(wallpaperDrawer, 1000 / FRAME_RATE);
        }

        private void stopDrawing() {
            wallpaperDrawingHandler.removeCallbacks(wallpaperDrawer);
        }

        @Override
        public void onWallpaperStateChanged(WallpaperState state) {
            this.state = state;
        }
    }

    static class TrackGestureListener extends FlingGestureListener {
        private SoundWallWallpaperServiceEngine engine;

        public TrackGestureListener(SoundWallWallpaperServiceEngine engine) {
            this.engine = engine;
        }

        @Override
        public void onSwipeLeft() {
            engine.wallpaperStateController.selectNextTrack();
        }

        @Override
        public void onSwipeRight() {
            engine.wallpaperStateController.selectPreviousTrack();
        }
    }

    static final class SoundWallWallpaperDrawer implements Runnable {
        private SoundWallWallpaperServiceEngine engine;

        public SoundWallWallpaperDrawer(SoundWallWallpaperServiceEngine engine) {
            this.engine = engine;
        }

        public void run() {
            engine.draw();
        }
    }
}

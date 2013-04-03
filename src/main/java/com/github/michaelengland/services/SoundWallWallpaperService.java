package com.github.michaelengland.services;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.wallpaper.SoundWallArtist;

import javax.inject.Inject;

public class SoundWallWallpaperService extends WallpaperService {
    private static final String TAG = "SoundWallWallpaperService";
    private static final int FRAME_RATE = 50;

    @Inject
    SoundWallArtist artist;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
        SoundWallApplication.getInstance().inject(this);
    }

    @Override
    public Engine onCreateEngine() {
        Log.d(TAG, "onCreateEngine()");
        return new SoundWallWallpaperServiceEngine(new Handler());
    }

    class SoundWallWallpaperServiceEngine extends WallpaperService.Engine {
        private static final String TAG = "SoundWallWallpaperServiceEngine";
        boolean visible;
        Handler wallpaperDrawingHandler;
        Runnable wallpaperDrawer;

        SoundWallWallpaperServiceEngine(Handler wallpaperDrawingHandler) {
            this.wallpaperDrawingHandler = wallpaperDrawingHandler;
            this.wallpaperDrawer = new SoundWallWallpaperDrawer(this);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            Log.d(TAG, "onCreate()");
            super.onCreate(surfaceHolder);
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

        void draw() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    artist.draw(canvas);
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

package com.github.michaelengland.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.github.michaelengland.R;
import com.github.michaelengland.entities.Track;

public class SoundWallArtistImpl implements SoundWallArtist {
    private Paint backgroundPaint;
    private Paint textPaint;
    private Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void draw(WallpaperState state, Canvas canvas) {
        drawBackground(canvas);
        drawWaveform(state, canvas);
        drawTitle(state, canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawPaint(getBackgroundPaint());
    }

    private Paint getBackgroundPaint() {
        if (backgroundPaint == null) {
            backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.BLACK);
            backgroundPaint.setStyle(Paint.Style.FILL);
        }
        return backgroundPaint;
    }

    private void drawTitle(WallpaperState state, Canvas canvas) {
        drawText(canvas, titleForState(state), canvas.getWidth() / 2, canvas.getHeight() / 4);
    }

    private String titleForState(WallpaperState state) {
        if (state.isLoggedIn()) {
            if (state.isLoadingTrack()) {
                return loadingText();
            }
            Track track = state.getTrack();
            if (track == null) {
                return noTrackText();
            } else {
                return textForTrack(track);
            }
        } else {
            return loggedOutText();
        }
    }

    private String loadingText() {
        return getString(R.string.loading_track);
    }

    private String noTrackText() {
        return getString(R.string.no_track);
    }

    private String textForTrack(Track track) {
        return track.getTitle();
    }

    private String loggedOutText() {
        return getString(R.string.logged_out);
    }

    private void drawText(Canvas canvas, String text, float x, float y) {
        canvas.drawText(text, x, y, getTextPaint());
    }

    private Paint getTextPaint() {
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setColor(Color.RED);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(20.0f);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        return textPaint;
    }

    private void drawWaveform(WallpaperState state, Canvas canvas) {
        if (state.isLoggedIn() && !state.isLoadingTrack()) {
            if (state.isLoadingWaveformImage()) {
                drawLoadingWaveformText(canvas);
            } else {
                Bitmap waveformImage = state.getWaveformImage();
                if (waveformImage == null) {
                    drawNoWaveformText(canvas);
                } else {
                    drawWaveformImage(waveformImage, canvas);
                }
            }
        }
    }

    private void drawLoadingWaveformText(Canvas canvas) {
        drawWaveformText(R.string.loading_waveform, canvas);
    }

    private void drawNoWaveformText(Canvas canvas) {
        drawWaveformText(R.string.no_waveform, canvas);
    }

    private void drawWaveformText(int resId, Canvas canvas) {
        drawText(canvas, getString(resId), canvas.getWidth() / 2, (canvas.getHeight() / 4) * 3);
    }

    private String getString(int resId) {
        if (context != null) {
            return context.getString(resId);
        } else {
            return "";
        }
    }

    private void drawWaveformImage(Bitmap waveformImage, Canvas canvas) {
        Bitmap screenBitmap = Bitmap.createScaledBitmap(waveformImage, canvas.getWidth(),
                canvas.getHeight(), true);
        canvas.drawBitmap(screenBitmap, 0, 0, getTextPaint());
    }
}

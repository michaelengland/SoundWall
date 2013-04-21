package com.github.michaelengland.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.animation.*;
import com.github.michaelengland.R;
import com.github.michaelengland.entities.Track;

public class SoundWallArtistImpl implements SoundWallArtist {
    private static final int WAVEFORM_WIDTH = 1800;
    private static final int WAVEFORM_HEIGHT = 280;
    private static final int ANIMATION_DURATION = 8000;

    private Paint backgroundPaint;
    private TextPaint textPaint;
    private Context context;
    private WallpaperState currentState;
    private Animation waveformAnimation;
    private Transformation transformation;

    public SoundWallArtistImpl() {
        transformation = new Transformation();
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void draw(WallpaperState state, Canvas canvas) {
        if ((currentState != null) && (!currentState.equals(state))) {
            getWaveformAnimation().reset();
        }
        currentState = state;
        drawBackground(canvas);
        drawWaveform(canvas);
        drawTitle(canvas);
    }

    private Animation getWaveformAnimation() {
        if (waveformAnimation == null) {
            waveformAnimation = new TranslateAnimation(0, -WAVEFORM_WIDTH, 0, 0);
            waveformAnimation.setDuration(ANIMATION_DURATION);
            waveformAnimation.setRepeatCount(TranslateAnimation.INFINITE);
            waveformAnimation.setRepeatMode(TranslateAnimation.RESTART);
            waveformAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
            waveformAnimation.setInterpolator(new LinearInterpolator());
            waveformAnimation.initialize(WAVEFORM_WIDTH, WAVEFORM_HEIGHT, WAVEFORM_WIDTH, WAVEFORM_HEIGHT);
        }
        return waveformAnimation;
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

    private void drawTitle(Canvas canvas) {
        drawText(canvas, title(), canvas.getWidth() / 2, canvas.getHeight() / 4);
    }

    private String title() {
        if (currentState.isLoggedIn()) {
            if (currentState.isLoadingTrack()) {
                return loadingText();
            }
            Track track = currentState.getTrack();
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

    private TextPaint getTextPaint() {
        if (textPaint == null) {
            textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.RED);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(20.0f);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        return textPaint;
    }

    private void drawWaveform(Canvas canvas) {
        if (currentState.isLoggedIn() && !currentState.isLoadingTrack()) {
            if (currentState.isLoadingWaveformImage()) {
                drawLoadingWaveformText(canvas);
            } else {
                Bitmap waveformImage = currentState.getWaveformImage();
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
        getWaveformAnimation().getTransformation(AnimationUtils.currentAnimationTimeMillis(), transformation);
        int startY = (canvas.getHeight() / 2) - (waveformImage.getHeight() / 2);
        transformation.getMatrix().postTranslate(0, startY);
        canvas.drawBitmap(waveformImage, transformation.getMatrix(), getTextPaint());
    }
}

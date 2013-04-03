package com.github.michaelengland.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SoundWallArtistImpl implements SoundWallArtist {
    private Paint backgroundPaint;
    private Paint textPaint;

    public void draw(Canvas canvas) {
        canvas.drawPaint(getBackgroundPaint());
        canvas.drawText("Mike is testing!", canvas.getWidth() / 2, canvas.getHeight() / 2, getTextPaint());
    }

    private Paint getBackgroundPaint() {
        if (backgroundPaint == null) {
            backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.BLACK);
            backgroundPaint.setStyle(Paint.Style.FILL);
        }
        return backgroundPaint;
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
}

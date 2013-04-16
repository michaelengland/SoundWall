package com.github.michaelengland.wallpaper;

import android.content.Context;
import android.graphics.Canvas;

public interface SoundWallArtist {
    void setContext(Context context);

    void draw(WallpaperState state, Canvas canvas);
}

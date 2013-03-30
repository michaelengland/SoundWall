package com.github.michaelengland.services;

import android.service.wallpaper.WallpaperService;

public class SoundWallWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new SoundWallWallpaperServiceEngine();
    }

    class SoundWallWallpaperServiceEngine extends WallpaperService.Engine {

    }
}

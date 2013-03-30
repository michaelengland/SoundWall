package com.github.michaelengland.services;

import android.service.wallpaper.WallpaperService;
import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.utils.AppLogger;

import javax.inject.Inject;

public class SoundWallWallpaperService extends WallpaperService {
    @Inject
    AppLogger logger;

    @Override
    public void onCreate() {
        super.onCreate();
        SoundWallApplication app = (SoundWallApplication) getApplication();
        app.inject(this);
        logger.log("Wallpaper created");
    }

    @Override
    public Engine onCreateEngine() {
        return new SoundWallWallpaperServiceEngine();
    }

    class SoundWallWallpaperServiceEngine extends WallpaperService.Engine {

    }
}

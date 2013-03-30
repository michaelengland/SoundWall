package com.github.michaelengland;

import com.github.michaelengland.services.SoundWallWallpaperService;
import com.github.michaelengland.utils.AppLogger;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        entryPoints = {
                SoundWallApplication.class,
                SoundWallWallpaperService.class
        }
)
public class SoundWallModule {
    @Provides
    @Singleton
    AppLogger provideAppLogger() {
        return new AppLogger();
    }
}

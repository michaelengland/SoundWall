package com.github.michaelengland.modules;

import com.github.michaelengland.SoundWallApplication;
import com.github.michaelengland.activities.SettingsActivity;
import com.github.michaelengland.fragments.LoginInputFragment;
import com.github.michaelengland.fragments.LoginTaskFragment;
import com.github.michaelengland.services.SoundWallWallpaperService;
import com.github.michaelengland.wallpaper.SoundWallArtist;
import com.github.michaelengland.wallpaper.SoundWallArtistImpl;
import dagger.Module;
import dagger.Provides;

@Module(
        entryPoints = {
                SoundWallApplication.class,
                SoundWallWallpaperService.class,
                SettingsActivity.class,
                LoginTaskFragment.class,
                LoginInputFragment.class
        },
        includes = {
                AndroidModule.class,
                SoundWallApiModule.class
        }
)
public class SoundWallModule {
    @Provides
    SoundWallArtist provideSoundWallArtist() {
        return new SoundWallArtistImpl();
    }
}

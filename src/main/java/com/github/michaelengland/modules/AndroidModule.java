package com.github.michaelengland.modules;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.github.michaelengland.SoundWallApplication;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AndroidModule {
    @Provides
    @Singleton
    Context provideAppContext() {
        return SoundWallApplication.getInstance().getApplicationContext();
    }

    @Provides
    Resources provideAppResources(final Context context) {
        return context.getResources();
    }

    @Provides
    AssetManager provideAssetManager(final Resources resources) {
        return resources.getAssets();
    }
}

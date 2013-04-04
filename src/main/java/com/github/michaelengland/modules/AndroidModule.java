package com.github.michaelengland.modules;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
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

    @Provides
    SharedPreferences provideSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    AccountManager provideAccountManager(final Context context) {
        return AccountManager.get(context);
    }
}

package com.github.michaelengland;

import android.app.Application;
import com.github.michaelengland.modules.SoundWallModule;
import dagger.ObjectGraph;

public class SoundWallApplication extends Application {
    private static SoundWallApplication instance;

    private ObjectGraph objectGraph;

    public static SoundWallApplication getInstance() {
        if (instance == null) {
            instance = new SoundWallApplication();
            instance.initialize();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initialize();
    }

    private void initialize() {
        objectGraph = ObjectGraph.create(getRootModule());
        inject(this);
    }

    private Object getRootModule() {
        return new SoundWallModule();
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}

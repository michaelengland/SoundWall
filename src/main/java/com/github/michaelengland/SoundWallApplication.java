package com.github.michaelengland;

import android.app.Application;
import dagger.ObjectGraph;

public class SoundWallApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
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

package com.github.michaelengland.wallpaper;

import android.graphics.Bitmap;
import com.github.michaelengland.entities.Track;

public class WallpaperState {
    private volatile boolean loggedIn;
    private volatile Track track;
    private volatile boolean loadingTrack;
    private volatile Bitmap waveformImage;
    private volatile boolean loadingWaveformImage;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public boolean isLoadingTrack() {
        return loadingTrack;
    }

    public void setLoadingTrack(boolean loadingTrack) {
        this.loadingTrack = loadingTrack;
    }

    public Bitmap getWaveformImage() {
        return waveformImage;
    }

    public void setWaveformImage(Bitmap waveFormImage) {
        this.waveformImage = waveFormImage;
    }

    public boolean isLoadingWaveformImage() {
        return loadingWaveformImage;
    }

    public void setLoadingWaveformImage(boolean loadingWaveformImage) {
        this.loadingWaveformImage = loadingWaveformImage;
    }

    public void reset() {
        loggedIn = false;
        track = null;
        loadingTrack = false;
        waveformImage = null;
        loadingWaveformImage = false;
    }
}

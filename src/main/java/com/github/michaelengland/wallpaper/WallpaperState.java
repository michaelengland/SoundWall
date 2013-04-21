package com.github.michaelengland.wallpaper;

import android.graphics.Bitmap;
import com.github.michaelengland.entities.Track;

public class WallpaperState {
    private volatile boolean loggedIn;
    private volatile Track track;
    private volatile boolean loadingTrack;
    private volatile Bitmap waveformImage;
    private volatile boolean loadingWaveformImage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WallpaperState that = (WallpaperState) o;

        if (loadingTrack != that.loadingTrack) return false;
        if (loadingWaveformImage != that.loadingWaveformImage) return false;
        if (loggedIn != that.loggedIn) return false;
        if (track != null ? !track.equals(that.track) : that.track != null) return false;
        if (waveformImage != null ? !waveformImage.equals(that.waveformImage) : that.waveformImage != null)
            return false;

        return true;
    }

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

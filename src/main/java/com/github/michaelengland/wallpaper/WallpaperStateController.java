package com.github.michaelengland.wallpaper;

import android.graphics.Bitmap;
import com.github.michaelengland.api.GetTracksTask;
import com.github.michaelengland.api.GetWaveformTask;
import com.github.michaelengland.entities.Track;
import com.github.michaelengland.managers.LoginManager;
import com.github.michaelengland.managers.UserStateChangeEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class WallpaperStateController {
    @Inject
    Bus bus;
    @Inject
    LoginManager loginManager;
    @Inject
    Provider<GetTracksTask> getTracksTaskProvider;
    private GetTracksTask currentGetTracksTask;
    @Inject
    Provider<GetWaveformTask> getWaveformTaskProvider;
    private GetWaveformTask currentGetWaveformTask;

    private volatile boolean active;
    private volatile WallpaperState state;
    private volatile List<Track> tracks;

    private WallpaperStateChangeListener listener;

    public WallpaperStateController() {
        state = new WallpaperState();
    }

    public void setListener(WallpaperStateChangeListener listener) {
        this.listener = listener;
    }

    public WallpaperState getState() {
        return state;
    }

    public void start() {
        active = true;
        bus.register(this);
        setupForUser();
    }

    public void stop() {
        active = false;
        bus.unregister(this);
        stopTasks();
    }

    private void stopTasks() {
        cancelTrackRetrieval();
        cancelWaveformImageRetrieval();
    }

    @Subscribe
    public void onUserStateChange(UserStateChangeEvent event) {
        setupForUser();
    }

    private void setupForUser() {
        state.setLoggedIn(loginManager.isLoggedIn());
        if (loginManager.isLoggedIn()) {
            onLoggedIn();
        } else {
            onLoggedOut();
        }
        triggerListener();
    }

    private void onLoggedIn() {
        retrieveTracks();
    }

    private void onLoggedOut() {
        stopTasks();
        state.reset();
        triggerListener();
    }

    private void retrieveTracks() {
        if (active) {
            state.setLoadingTrack(true);
            currentGetTracksTask = getTracksTaskProvider.get();
            currentGetTracksTask.setListener(new TracksListener(this));
            currentGetTracksTask.execute();
            triggerListener();
        }
    }

    private void cancelTrackRetrieval() {
        if (currentGetTracksTask != null) {
            currentGetTracksTask.cancel(true);
        }
    }

    private void cancelWaveformImageRetrieval() {
        if (currentGetWaveformTask != null) {
            currentGetWaveformTask.cancel(true);
        }
    }

    private void setActiveTrack(final Track track) {
        state.setTrack(track);
        state.setWaveformImage(null);
        cancelWaveformImageRetrieval();
        triggerListener();
        retrieveWaveformImage();
    }

    private void retrieveWaveformImage() {
        if (active) {
            state.setLoadingWaveformImage(true);
            currentGetWaveformTask = getWaveformTaskProvider.get();
            currentGetWaveformTask.setListener(new WaveformListener(this));
            currentGetWaveformTask.setTrack(state.getTrack());
            currentGetWaveformTask.execute();
            triggerListener();
        }
    }

    private void setActiveWaveform(final Bitmap waveform) {
        state.setWaveformImage(waveform);
        triggerListener();
    }

    private void triggerListener() {
        if (listener != null) {
            listener.onWallpaperStateChanged(state);
        }
    }

    public interface WallpaperStateChangeListener {
        void onWallpaperStateChanged(WallpaperState state);
    }

    static class TracksListener implements GetTracksTask.GetTracksTaskListener {
        private WallpaperStateController controller;

        TracksListener(WallpaperStateController controller) {
            this.controller = controller;
        }

        @Override
        public void onGetTracksSuccess(List<Track> tracks) {
            setNotLoadingTrack();
            setTracks(tracks);
        }

        @Override
        public void onGetTracksFailure() {
            setNotLoadingTrack();
            retryTrackRetrieval();
        }

        private void setTracks(List<Track> tracks) {
            controller.tracks = tracks;
            if (tracks.isEmpty()) {
                controller.setActiveTrack(null);
            } else {
                controller.setActiveTrack(tracks.get(0));
            }
        }

        private void retryTrackRetrieval() {
            controller.retrieveTracks();
        }

        private void setNotLoadingTrack() {
            controller.state.setLoadingTrack(false);
            controller.triggerListener();
        }
    }

    static class WaveformListener implements GetWaveformTask.GetWaveformTaskListener {
        private WallpaperStateController controller;

        WaveformListener(WallpaperStateController controller) {
            this.controller = controller;
        }

        @Override
        public void onGetWaveformSuccess(final Bitmap waveform) {
            controller.setActiveWaveform(waveform);
            setNotLoadingWaveformImage();
        }

        @Override
        public void onGetWaveformFailure() {
            setNotLoadingWaveformImage();
            retryWaveformImageRetrieval();
        }

        private void retryWaveformImageRetrieval() {
            controller.retrieveWaveformImage();
        }

        private void setNotLoadingWaveformImage() {
            controller.state.setLoadingWaveformImage(false);
            controller.triggerListener();
        }
    }
}

package com.github.michaelengland.entities;

import android.net.Uri;

public class Track {
    private String title;
    private Uri waveformUri;

    public Track(final String title, final Uri waveformUri) {
        this.title = title;
        this.waveformUri = waveformUri;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Track) {
            Track otherTrack = (Track) o;
            return (otherTrack.title.equals(title)) &&
                    (otherTrack.waveformUri.equals(otherTrack.waveformUri));
        } else {
            return false;
        }
    }

    public String getTitle() {
        return title;
    }

    public Uri getWaveformUri() {
        return waveformUri;
    }
}

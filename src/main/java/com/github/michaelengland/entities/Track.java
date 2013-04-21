package com.github.michaelengland.entities;

public class Track {
    private String title;
    private Url waveformUrl;

    public Track(final String title, final Url waveformUrl) {
        this.title = title;
        this.waveformUrl = waveformUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Track) {
            Track otherTrack = (Track) o;
            return (otherTrack.title.equals(title)) &&
                    (otherTrack.waveformUrl.equals(waveformUrl));
        } else {
            return false;
        }
    }

    public String getTitle() {
        return title;
    }

    public Url getWaveformUrl() {
        return waveformUrl;
    }
}

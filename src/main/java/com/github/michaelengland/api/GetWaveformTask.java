package com.github.michaelengland.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.github.michaelengland.entities.Track;

import javax.inject.Inject;

public class GetWaveformTask extends AsyncTask<Void, Void, Bitmap> {
    @Inject
    SoundCloudClient client;

    private GetWaveformTaskListener listener;
    private Track track;

    public void setTrack(Track track) {
        this.track = track;
    }

    public void setListener(GetWaveformTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            return client.getWaveformForTrack(track);
        } catch (SoundCloudClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap waveform) {
        if (listener != null) {
            if (waveform != null) {
                listener.onGetWaveformSuccess(waveform);
            } else {
                listener.onGetWaveformFailure();
            }
        }
    }

    public static interface GetWaveformTaskListener {
        void onGetWaveformSuccess(final Bitmap waveform);

        void onGetWaveformFailure();
    }
}

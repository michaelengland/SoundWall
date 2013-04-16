package com.github.michaelengland.api;

import android.os.AsyncTask;
import com.github.michaelengland.entities.Track;

import javax.inject.Inject;
import java.util.List;

public class GetTracksTask extends AsyncTask<Void, Void, List<Track>> {
    @Inject
    SoundCloudClient client;

    private GetTracksTaskListener listener;

    public void setListener(GetTracksTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Track> doInBackground(Void... voids) {
        try {
            return client.getTracks();
        } catch (SoundCloudClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        if (listener != null) {
            if (tracks != null) {
                listener.onGetTracksSuccess(tracks);
            } else {
                listener.onGetTracksFailure();
            }
        }
    }

    public static interface GetTracksTaskListener {
        void onGetTracksSuccess(final List<Track> tracks);

        void onGetTracksFailure();
    }
}

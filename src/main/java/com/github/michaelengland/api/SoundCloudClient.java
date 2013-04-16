package com.github.michaelengland.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.github.michaelengland.entities.Track;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;
import org.apache.http.HttpResponse;
import org.json.JSONException;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SoundCloudClient {
    ApiWrapper apiWrapper;
    TracksParser tracksParser;

    @Inject
    SoundCloudClient(final ApiWrapper apiWrapper, final TracksParser tracksParser) {
        this.apiWrapper = apiWrapper;
        this.tracksParser = tracksParser;
    }

    public Token login(String username, String password) throws SoundCloudClientException {
        try {
            return performLogin(username, password);
        } catch (IOException e) {
            throw new SoundCloudClientException(e);
        }
    }

    private Token performLogin(String username, String password) throws IOException {
        return apiWrapper.login(username, password);
    }

    public List<Track> getTracks() throws SoundCloudClientException {
        try {
            return performGetTracks();
        } catch (IOException e) {
            throw new SoundCloudClientException(e);
        } catch (JSONException e) {
            throw new SoundCloudClientException(e);
        }
    }

    private List<Track> performGetTracks() throws IOException, JSONException {
        return tracksFromResponse(apiWrapper.get(new Request("me/favorites")));
    }

    private List<Track> tracksFromResponse(HttpResponse response) throws IOException, JSONException {
        return tracksParser.parseTracks(response.getEntity().getContent());
    }

    public Bitmap getWaveformForTrack(Track track) throws SoundCloudClientException {
        try {
            return performGetWaveformForTrack(track);
        } catch (IOException e) {
            throw new SoundCloudClientException(e);
        }
    }

    private Bitmap performGetWaveformForTrack(Track track) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(track.getWaveformUri().toString()).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        connection.disconnect();
        return bitmap;
    }
}

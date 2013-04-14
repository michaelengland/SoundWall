package com.github.michaelengland.api;

import android.net.Uri;
import com.github.michaelengland.entities.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TracksParser {
    public List<Track> parseTracks(InputStream inputStream) throws IOException, JSONException {
        return tracksFromJson(jsonFromString(stringFromStream(inputStream)));
    }

    private List<Track> tracksFromJson(final JSONArray json) throws JSONException {
        List<Track> tracks = new ArrayList<Track>(json.length());
        for (int i = 0; i < json.length(); i++) {
            tracks.add(trackFromJson(json.getJSONObject(i)));
        }
        return tracks;
    }

    private Track trackFromJson(final JSONObject json) throws JSONException {
        String title = json.getString("title");
        Uri waveformUrl = Uri.parse(json.getString("waveform_url"));
        return new Track(title, waveformUrl);
    }

    private JSONArray jsonFromString(final String string) throws JSONException {
        return new JSONArray(string);
    }

    private String stringFromStream(final InputStream inputStream) throws IOException {
        InputStreamReader is = new InputStreamReader(inputStream);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();
        while (read != null) {
            sb.append(read);
            read = br.readLine();
        }
        return sb.toString();
    }
}

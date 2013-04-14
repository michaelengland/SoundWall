package com.github.michaelengland.api;

import android.net.Uri;
import com.github.michaelengland.entities.Track;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class TracksParserTest {
    private TracksParser subject;
    private List<Track> tracks;

    @Before
    public void setUp() throws Exception {
        tracks = new ArrayList<Track>(2);
        tracks.add(new Track("Running CS-15 Through Itself",
                Uri.parse("http://w1.sndcdn.com/BHaUCkUiJzN5_m.png")));
        tracks.add(new Track("Katyperryrmx work in progress",
                Uri.parse("http://w1.sndcdn.com/S0Gse1m7NnG3_m.png")));
        subject = new TracksParser();
    }

    @Test
    public void testParseTracksReturnsTrackList() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/tracks.json");

        List<Track> parsedTracks = subject.parseTracks(inputStream);
        Assert.assertThat(parsedTracks, CoreMatchers.notNullValue());
        Assert.assertThat(parsedTracks.size(), CoreMatchers.equalTo(2));
        Assert.assertThat(parsedTracks.get(0), CoreMatchers.equalTo(tracks.get(0)));
        Assert.assertThat(parsedTracks.get(1), CoreMatchers.equalTo(tracks.get(1)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testParseTracksThrowsIOExceptionWhenInputStreamProblem() throws Exception {
        InputStream inputStream = PowerMockito.mock(InputStream.class);
        Mockito.when(inputStream.read()).thenThrow(IOException.class);

        try {
            subject.parseTracks(inputStream);
            Assert.fail();
        } catch (IOException e) {
        }
    }

    @Test
    public void testTestParseTracksThrowsJSONExceptionWhenInvalidJson() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/invalid.json");

        try {
            subject.parseTracks(inputStream);
            Assert.fail();
        } catch (JSONException e) {
        }
    }

    @Test
    public void testTestParseTracksThrowsJSONExceptionWhenWaveformNotFound() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/missing.json");

        try {
            subject.parseTracks(inputStream);
            Assert.fail();
        } catch (JSONException e) {
        }
    }
}

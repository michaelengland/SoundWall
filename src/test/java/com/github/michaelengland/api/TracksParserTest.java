package com.github.michaelengland.api;

import com.github.michaelengland.entities.Track;
import com.github.michaelengland.entities.Url;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class TracksParserTest {
    private TracksParser subject;
    private List<Track> tracks;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tracks = new ArrayList<Track>(2);
        tracks.add(new Track("Running CS-15 Through Itself",
                new Url("http://w1.sndcdn.com/BHaUCkUiJzN5_m.png")));
        tracks.add(new Track("Katyperryrmx work in progress",
                new Url("http://w1.sndcdn.com/S0Gse1m7NnG3_m.png")));
        subject = new TracksParser();
    }

    @Test
    public void testParseTracksReturnsTrackList() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/tracks.json");

        List<Track> parsedTracks = subject.parseTracks(inputStream);
        assertThat(parsedTracks, notNullValue());
        assertThat(parsedTracks.size(), equalTo(2));
        assertThat(parsedTracks.get(0), equalTo(tracks.get(0)));
        assertThat(parsedTracks.get(1), equalTo(tracks.get(1)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IOException.class)
    public void testParseTracksThrowsIOExceptionWhenInputStreamProblem() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        doThrow(IOException.class).when(inputStream).read();
        subject.parseTracks(inputStream);
    }

    @Test(expected = JSONException.class)
    public void testTestParseTracksThrowsJSONExceptionWhenInvalidJson() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/invalid.json");
        subject.parseTracks(inputStream);
    }

    @Test(expected = JSONException.class)
    public void testTestParseTracksThrowsJSONExceptionWhenWaveformNotFound() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/missing.json");
        subject.parseTracks(inputStream);
    }
}

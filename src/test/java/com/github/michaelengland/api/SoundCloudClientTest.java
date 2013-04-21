package com.github.michaelengland.api;

import com.github.michaelengland.entities.Track;
import com.github.michaelengland.entities.Url;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class SoundCloudClientTest {
    private SoundCloudClient subject;
    @Mock
    private ApiWrapper apiWrapper;
    @Mock
    private TracksParser tracksParser;
    @Mock
    private InputStream inputStream;
    @Mock
    private Token token;
    private List<Track> tracks;
    @Mock
    private HttpURLConnection connection;
    private Track track;
    @Mock
    private Url url;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        HttpResponse response = mock(HttpResponse.class);
        tracks = Collections.emptyList();
        track = new Track("title", url);
        HttpEntity entity = mock(HttpEntity.class);
        doReturn(token).when(apiWrapper).login("user", "pass");
        subject = new SoundCloudClient(apiWrapper, tracksParser);
        doReturn(inputStream).when(entity).getContent();
        doReturn(entity).when(response).getEntity();
        doReturn(response).when(apiWrapper).get(any(Request.class));
        doReturn(tracks).when(tracksParser).parseTracks(inputStream);
        doReturn(connection).when(url).openConnection();
        doReturn(inputStream).when(connection).getInputStream();
    }

    @Test
    public void testShouldDelegatesLoginTokenRequestToApiWrapper() throws Exception {
        assertThat(subject.login("user", "pass"), equalTo(token));
    }

    @Test(expected = SoundCloudClientException.class)
    public void testShouldThrowClientExceptionWhenApiWrapperThrowsExceptionOnLogin() throws Exception {
        doThrow(new IOException()).when(apiWrapper).login("user", "pass");
        subject.login("user", "pass");
    }

    @Test
    public void testShouldRequestCorrectTrackFavoritesUrl() throws Exception {
        subject.getTracks();
        verify(apiWrapper).get(argThat(new UserFavoritesRequestMatcher()));
    }

    @Test
    public void testShouldRetrieveParsedFromFavouritesTracks() throws Exception {
        assertThat(subject.getTracks(), equalTo(tracks));
    }

    @Test(expected = SoundCloudClientException.class)
    public void testShouldThrowClientExceptionWhenApiWrapperThrowsIOExceptionOnGetTracks() throws Exception {
        doThrow(new IOException()).when(apiWrapper).get(any(Request.class));
        subject.getTracks();
    }

    @Test(expected = SoundCloudClientException.class)
    public void testShouldThrowClientExceptionWhenTracksParserThrowsIOExceptionOnGetTracks() throws Exception {
        doThrow(new IOException()).when(tracksParser).parseTracks(inputStream);
        subject.getTracks();
    }

    @Test(expected = SoundCloudClientException.class)
    public void testShouldThrowClientExceptionWhenTracksParserThrowsJSONExceptionOnGetTracks() throws Exception {
        doThrow(new JSONException("")).when(tracksParser).parseTracks(inputStream);
        subject.getTracks();
    }

    @Test
    public void testShouldRetrieveBitmapTrackWaveformForTrack() throws Exception {
        assertThat(subject.getWaveformForTrack(track), notNullValue());
    }

    @Test
    public void testShouldUseTrackWaveformUrlAddress() throws Exception {
        subject.getWaveformForTrack(track);
        verify(url).openConnection();
    }

    @Test
    public void testShouldDisconnectAfterWaveformRetrieval() throws Exception {
        subject.getWaveformForTrack(track);
        verify(connection).disconnect();
    }

    @Test(expected = SoundCloudClientException.class)
    public void testShouldDisconnectAfterWaveformRetrievalError() throws Exception {
        doThrow(IOException.class).when(connection).getInputStream();
        subject.getWaveformForTrack(track);
        verify(connection).disconnect();
    }

    static class UserFavoritesRequestMatcher extends ArgumentMatcher<Request> {
        @Override
        public boolean matches(Object argument) {
            if (argument instanceof Request) {
                Request request = (Request) argument;
                return request.toString().equals(new Request("me/favorites").toString());
            } else {
                return false;
            }
        }
    }
}

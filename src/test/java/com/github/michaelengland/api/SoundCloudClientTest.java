package com.github.michaelengland.api;

import com.github.michaelengland.entities.Track;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class SoundCloudClientTest {
    private SoundCloudClient subject;
    private ApiWrapper apiWrapper;
    private TracksParser tracksParser;
    private HttpResponse response;
    private InputStream inputStream;
    private Token token;
    private List<Track> tracks;

    @Before
    public void setUp() throws Exception {
        apiWrapper = PowerMockito.mock(ApiWrapper.class);
        token = PowerMockito.mock(Token.class);
        tracksParser = PowerMockito.mock(TracksParser.class);
        response = PowerMockito.mock(HttpResponse.class);
        inputStream = PowerMockito.mock(InputStream.class);
        tracks = new ArrayList<Track>(0);
        HttpEntity entity = PowerMockito.mock(HttpEntity.class);
        Mockito.when(apiWrapper.login("user", "pass")).thenReturn(token);
        subject = new SoundCloudClient(apiWrapper, tracksParser);
        Mockito.when(entity.getContent()).thenReturn(inputStream);
        Mockito.when(response.getEntity()).thenReturn(entity);
        Mockito.when(apiWrapper.get(Mockito.any(Request.class))).thenReturn(response);
        Mockito.when(tracksParser.parseTracks(inputStream)).thenReturn(tracks);
    }

    @Test
    public void testShouldDelegatesLoginTokenRequestToApiWrapper() throws Exception {
        Assert.assertThat(subject.login("user", "pass"), CoreMatchers.equalTo(token));
    }

    @Test
    public void testShouldThrowClientExceptionWhenApiWrapperThrowsExceptionOnLogin() throws Exception {
        Mockito.when(apiWrapper.login("user", "pass")).thenThrow(new IOException());
        try {
            subject.login("user", "pass");
            Assert.fail();
        } catch (SoundCloudClientException e) {
        }
    }

    @Test
    public void testShouldRequestCorrectTrackFavoritesUrl() throws Exception {
        subject.getTracks();
        Mockito.verify(apiWrapper).get(Mockito.argThat(new UserFavoritesRequestMatcher()));
    }

    @Test
    public void testShouldRetrieveParsedFromFavouritesTracks() throws Exception {
        Assert.assertThat(subject.getTracks(), CoreMatchers.equalTo(tracks));
    }

    @Test
    public void testShouldThrowClientExceptionWhenApiWrapperThrowsIOExceptionOnGetTracks() throws Exception {
        Mockito.when(apiWrapper.get(Mockito.any(Request.class))).thenThrow(new IOException());
        try {
            subject.getTracks();
            Assert.fail();
        } catch (SoundCloudClientException e) {
        }
    }

    @Test
    public void testShouldThrowClientExceptionWhenTracksParserThrowsIOExceptionOnGetTracks() throws Exception {
        Mockito.when(tracksParser.parseTracks(inputStream)).thenThrow(new IOException());
        try {
            subject.getTracks();
            Assert.fail();
        } catch (SoundCloudClientException e) {
        }
    }

    @Test
    public void testShouldThrowClientExceptionWhenTracksParserThrowsJSONExceptionOnGetTracks() throws Exception {
        Mockito.when(tracksParser.parseTracks(inputStream)).thenThrow(new JSONException(""));
        try {
            subject.getTracks();
            Assert.fail();
        } catch (SoundCloudClientException e) {
        }
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

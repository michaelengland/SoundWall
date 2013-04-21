package com.github.michaelengland.api;

import com.github.michaelengland.entities.Track;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class GetTracksTaskTest {
    private TestableGetTracksTask subject;
    @Mock
    private SoundCloudClient client;
    private List<Track> tracks;
    @Mock
    private GetTracksTask.GetTracksTaskListener listener;

    static class TestableGetTracksTask extends GetTracksTask {
        @Override
        public List<Track> doInBackground(Void... voids) {
            return super.doInBackground(voids);
        }

        @Override
        public void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);
        }
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new TestableGetTracksTask();
        tracks = Collections.emptyList();
        subject.client = client;
        subject.setListener(listener);
    }

    @Test
    public void testShouldReturnClientTracksWhenSuccessful() throws Exception {
        doReturn(tracks).when(client).getTracks();
        assertThat(subject.doInBackground(), equalTo(tracks));
    }

    @Test
    public void testShouldReturnNullWhenFailure() throws Exception {
        doThrow(SoundCloudClientException.class).when(client).getTracks();
        assertThat(subject.doInBackground(), nullValue());
    }

    @Test
    public void testShouldFireListenerSuccessWhenListenerSetAndTracksRetrieved() throws Exception {
        subject.onPostExecute(tracks);
        verify(listener).onGetTracksSuccess(tracks);
    }

    @Test
    public void testShouldFireListenerFailureWhenListenerSetAndTracksNotRetrieved() throws Exception {
        subject.onPostExecute(null);
        verify(listener).onGetTracksFailure();
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndTracksRetrieved() throws Exception {
        try {
            subject.setListener(null);
            subject.onPostExecute(tracks);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndTracksNotRetrieved() throws Exception {
        try {
            subject.setListener(null);
            subject.onPostExecute(null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

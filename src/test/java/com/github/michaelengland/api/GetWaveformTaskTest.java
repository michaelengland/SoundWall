package com.github.michaelengland.api;

import android.graphics.Bitmap;
import com.github.michaelengland.entities.Track;
import com.github.michaelengland.entities.Url;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class GetWaveformTaskTest {
    private TestableGetWaveformTask subject;
    @Mock
    private SoundCloudClient client;
    private Track track;
    @Mock
    private Bitmap bitmap;
    @Mock
    private GetWaveformTask.GetWaveformTaskListener listener;

    static class TestableGetWaveformTask extends GetWaveformTask {
        @Override
        public Bitmap doInBackground(Void... voids) {
            return super.doInBackground(voids);
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new TestableGetWaveformTask();
        track = new Track("title", new Url("https://waveform"));
        subject.client = client;
        subject.setTrack(track);
        subject.setListener(listener);
    }

    @Test
    public void testShouldReturnClientBitmapWhenSuccessful() throws Exception {
        doReturn(bitmap).when(client).getWaveformForTrack(track);
        assertThat(subject.doInBackground(), equalTo(bitmap));
    }

    @Test
    public void testShouldReturnNullWhenFailure() throws Exception {
        doThrow(SoundCloudClientException.class).when(client).getTracks();
        assertThat(subject.doInBackground(), nullValue());
    }

    @Test
    public void testShouldFireListenerSuccessWhenListenerSetAndBitmapRetrieved() throws Exception {
        subject.onPostExecute(bitmap);
        verify(listener).onGetWaveformSuccess(bitmap);
    }

    @Test
    public void testShouldFireListenerFailureWhenListenerSetAndBitmapNotRetrieved() throws Exception {
        subject.onPostExecute(null);
        verify(listener).onGetWaveformFailure();
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndBitmapRetrieved() throws Exception {
        try {
            subject.setListener(null);
            subject.onPostExecute(bitmap);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndBitmapNotRetrieved() throws Exception {
        try {
            subject.setListener(null);
            subject.onPostExecute(null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

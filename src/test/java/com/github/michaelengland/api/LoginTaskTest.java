package com.github.michaelengland.api;

import com.soundcloud.api.Token;
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
public class LoginTaskTest {
    private TestableLoginTask subject;
    @Mock
    private SoundCloudClient client;
    @Mock
    private Token token;
    @Mock
    private LoginTask.LoginTaskListener listener;

    static class TestableLoginTask extends LoginTask {
        @Override
        public Token doInBackground(Void... voids) {
            return super.doInBackground(voids);
        }

        @Override
        public void onPostExecute(Token token) {
            super.onPostExecute(token);
        }
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new TestableLoginTask();
        subject.setUsername("user");
        subject.setPassword("pass");
        subject.client = client;
        subject.setListener(listener);
    }

    @Test
    public void testShouldReturnClientLoginTokenWhenSuccessful() throws Exception {
        doReturn(token).when(client).login("user", "pass");
        assertThat(subject.doInBackground(), equalTo(token));
    }

    @Test
    public void testShouldReturnNullLoginTokenWhenFailure() throws Exception {
        doThrow(SoundCloudClientException.class).when(client).login("user", "pass");
        assertThat(subject.doInBackground(), nullValue());
    }

    @Test
    public void testShouldFireListenerSuccessWhenListenerSetAndTokenRetrieved() throws Exception {
        subject.onPostExecute(token);
        verify(listener).onLoginSuccess(token);
    }

    @Test
    public void testShouldFireListenerFailureWhenListenerSetAndTokenNotRetrieved() throws Exception {
        subject.onPostExecute(null);
        verify(listener).onLoginFailure();
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndTokenRetrieved() throws Exception {
        try {
            subject.setListener(null);
            subject.onPostExecute(token);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndTokenNotRetrieved() throws Exception {
        try {
            subject.setListener(null);
            subject.onPostExecute(null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

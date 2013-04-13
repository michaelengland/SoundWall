package com.github.michaelengland.api;

import com.soundcloud.api.Token;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

@RunWith(RobolectricTestRunner.class)
public class LoginTaskTest {
    private TestableLoginTask subject;
    private SoundCloudClient client;
    private Token token;
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
        subject = new TestableLoginTask();
        subject.setUsername("user");
        subject.setPassword("pass");
        client = PowerMockito.mock(SoundCloudClient.class);
        token = PowerMockito.mock(Token.class);
        listener = PowerMockito.mock(LoginTask.LoginTaskListener.class);
        subject.client = client;
        subject.setListener(listener);
    }

    @Test
    public void testShouldReturnClientLoginTokenWhenSuccessful() throws Exception {
        Mockito.when(client.login("user", "pass")).thenReturn(token);
        Assert.assertThat(subject.doInBackground(), CoreMatchers.equalTo(token));
    }

    @Test
    public void testShouldReturnNullLoginTokenWhenFailure() throws Exception {
        Mockito.doThrow(SoundCloudClientException.class).when(client).login("user", "pass");
        Assert.assertThat(subject.doInBackground(), CoreMatchers.nullValue());
    }

    @Test
    public void testShouldFireListenerSuccessWhenListenerSetAndTokenRetrieved() throws Exception {
        subject.onPostExecute(token);
        Mockito.verify(listener).onLoginSuccess();
    }

    @Test
    public void testShouldFireListenerFailureWhenListenerSetAndTokenNotRetrieved() throws Exception {
        subject.onPostExecute(null);
        Mockito.verify(listener).onLoginFailure();
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndTokenRetrieved() throws Exception {
        subject.setListener(null);
        subject.onPostExecute(token);
    }

    @Test
    public void testShouldNotCrashWhenListenerNotSetAndTokenNotRetrieved() throws Exception {
        subject.setListener(null);
        subject.onPostExecute(null);
    }
}

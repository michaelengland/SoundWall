package com.github.michaelengland.api;

import com.github.michaelengland.managers.SettingsManager;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Token;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class SoundCloudClientTest {
    private SoundCloudClient subject;
    private ApiWrapper apiWrapper;
    private SettingsManager settingsManager;
    private Token token;

    @Before
    public void setUp() throws Exception {
        apiWrapper = PowerMockito.mock(ApiWrapper.class);
        token = PowerMockito.mock(Token.class);
        Mockito.when(apiWrapper.login("user", "pass")).thenReturn(token);
        settingsManager = PowerMockito.mock(SettingsManager.class);
        subject = new SoundCloudClient(apiWrapper, settingsManager);
    }

    @Test
    public void testShouldDelegatesLoginTokenRequestToApiWrapper() throws Exception {
        Assert.assertThat(subject.login("user", "pass"), CoreMatchers.equalTo(token));
    }

    @Test
    public void testShouldStoreLoginTokenInSettings() throws Exception {
        subject.login("user", "pass");
        Mockito.verify(settingsManager).setToken(token);
    }

    @Test
    public void testShouldThrowClientExceptionWhenApiWrapperThrowsException() throws Exception {
        Mockito.when(apiWrapper.login("user", "pass")).thenThrow(new IOException());
        try {
            subject.login("user", "pass");
            Assert.fail();
        } catch (SoundCloudClientException e) {
        }
    }
}

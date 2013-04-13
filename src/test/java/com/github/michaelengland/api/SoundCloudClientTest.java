package com.github.michaelengland.api;

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
    private Token token;

    @Before
    public void setUp() throws Exception {
        apiWrapper = PowerMockito.mock(ApiWrapper.class);
        token = PowerMockito.mock(Token.class);
        Mockito.when(apiWrapper.login("user", "pass")).thenReturn(token);
        subject = new SoundCloudClient(apiWrapper);
    }

    @Test
    public void testShouldDelegatesLoginTokenRequestToApiWrapper() throws Exception {
        Assert.assertThat(subject.login("user", "pass"), CoreMatchers.equalTo(token));
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

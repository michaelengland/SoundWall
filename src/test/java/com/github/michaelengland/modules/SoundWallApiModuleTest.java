package com.github.michaelengland.modules;

import android.content.res.AssetManager;
import com.github.michaelengland.api.TracksParser;
import com.soundcloud.api.ApiWrapper;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

@RunWith(RobolectricTestRunner.class)
public class SoundWallApiModuleTest {
    private SoundWallApiModule subject;
    private AssetManager assetManager;
    private String properties = "soundcloud.client_id = testId\n" +
            "soundcloud.client_secret = testSecret\n";
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        subject = new SoundWallApiModule();
        assetManager = PowerMockito.mock(AssetManager.class);
        inputStream = new ByteArrayInputStream(properties.getBytes());
        Mockito.when(assetManager.open("soundcloud.properties")).thenReturn(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        inputStream.close();
    }

    @Test
    public void testProvidesApiWrapper() throws Exception {
        Assert.assertThat(subject.provideApiWrapper(assetManager), CoreMatchers.instanceOf(ApiWrapper.class));
    }

    @Test
    public void testProvidesApiWrapperReadsApiClientInfoFromProperties() throws Exception {
        // Uses reflection because:
        // a) PowerMock doesn't work with Robolectric so no Constructor-mocking https://groups.google.com/forum/#!msg/powermock/2EhgfGHRBfY/98mhmn2hk_IJ
        // b) ApiWrapper doesn't reflect it's input parameters
        ApiWrapper apiWrapper = subject.provideApiWrapper(assetManager);
        Field clientIdField = apiWrapper.getClass().getDeclaredField("mClientId");
        clientIdField.setAccessible(true);
        Field clientSecretField = apiWrapper.getClass().getDeclaredField("mClientSecret");
        clientSecretField.setAccessible(true);
        Assert.assertThat((String) clientIdField.get(apiWrapper), CoreMatchers.equalTo("testId"));
        Assert.assertThat((String) clientSecretField.get(apiWrapper), CoreMatchers.equalTo("testSecret"));
    }

    @Test
    public void testThrowsRuntimeErrorWhenNoPropertiesFileSet() throws Exception {
        Mockito.when(assetManager.open("soundcloud.properties")).thenThrow(new IOException("File not found"));
        try {
            subject.provideApiWrapper(assetManager);
            Assert.fail();
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void testProvidesTracksParser() throws Exception {
        Assert.assertThat(subject.provideTracksParser(), CoreMatchers.instanceOf(TracksParser.class));
    }
}

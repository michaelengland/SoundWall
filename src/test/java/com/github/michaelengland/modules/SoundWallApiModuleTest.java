package com.github.michaelengland.modules;

import android.content.res.AssetManager;
import com.github.michaelengland.managers.SettingsManager;
import com.soundcloud.api.ApiWrapper;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(RobolectricTestRunner.class)
public class SoundWallApiModuleTest {
    private SoundWallApiModule subject;
    @Mock
    private AssetManager assetManager;
    @Mock
    private SettingsManager settingsManager;
    private String properties = "soundcloud.client_id = testId\n" +
            "soundcloud.client_secret = testSecret\n";
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new SoundWallApiModule();
        inputStream = new ByteArrayInputStream(properties.getBytes());
        doReturn(inputStream).when(assetManager).open("soundcloud.properties");
    }

    @After
    public void tearDown() throws Exception {
        inputStream.close();
    }

    @Test
    public void testProvidesApiWrapper() throws Exception {
        assertThat(subject.provideApiWrapper(assetManager, settingsManager), notNullValue());
    }

    @Test
    public void testProvidesApiWrapperReadsApiClientInfoFromProperties() throws Exception {
        // Uses reflection because:
        // a) PowerMock doesn't work with Robolectric so no Constructor-mocking https://groups.google.com/forum/#!msg/powermock/2EhgfGHRBfY/98mhmn2hk_IJ
        // b) ApiWrapper doesn't reflect it's input parameters
        ApiWrapper apiWrapper = subject.provideApiWrapper(assetManager, settingsManager);
        Field clientIdField = apiWrapper.getClass().getDeclaredField("mClientId");
        clientIdField.setAccessible(true);
        Field clientSecretField = apiWrapper.getClass().getDeclaredField("mClientSecret");
        clientSecretField.setAccessible(true);
        assertThat((String) clientIdField.get(apiWrapper), equalTo("testId"));
        assertThat((String) clientSecretField.get(apiWrapper), equalTo("testSecret"));
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsRuntimeErrorWhenNoPropertiesFileSet() throws Exception {
        doThrow(new IOException("File not found")).when(assetManager).open("soundcloud.properties");
        subject.provideApiWrapper(assetManager, settingsManager);
    }

    @Test
    public void testProvidesTracksParser() throws Exception {
        assertThat(subject.provideTracksParser(), notNullValue());
    }
}

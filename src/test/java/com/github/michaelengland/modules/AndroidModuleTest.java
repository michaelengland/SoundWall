package com.github.michaelengland.modules;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.github.michaelengland.SoundWallApplication;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
public class AndroidModuleTest {
    private AndroidModule subject;
    @Mock
    private Context context;
    @Mock
    private Resources resources;
    @Mock
    private AssetManager assetManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new AndroidModule();
    }

    @Test
    public void testShouldProvideContextFromApplication() throws Exception {
        assertThat(subject.provideAppContext(), equalTo(SoundWallApplication.getInstance().getApplicationContext()));
    }

    @Test
    public void testShouldProvideResourcesViaContext() throws Exception {
        doReturn(resources).when(context).getResources();
        assertThat(subject.provideAppResources(context), equalTo(resources));
    }

    @Test
    public void testShouldProvideAssetManagerViaResources() throws Exception {
        doReturn(assetManager).when(resources).getAssets();
        assertThat(subject.provideAssetManager(resources), equalTo(assetManager));
    }

    @Test
    public void testShouldProvideDefaultSharedPreferences() throws Exception {
        // Can't mock static methods without PowerMock
        Context context = subject.provideAppContext();
        assertThat(subject.provideSharedPreferences(context), notNullValue());
    }

    @Test
    public void testShouldProvideAccountManager() throws Exception {
        // Can't mock static methods without PowerMock
        Context context = subject.provideAppContext();
        assertThat(subject.provideAccountManager(context), notNullValue());
    }
}

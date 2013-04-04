package com.github.michaelengland.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.github.michaelengland.SoundWallApplication;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

@RunWith(RobolectricTestRunner.class)
public class AndroidModuleTest {
    private AndroidModule subject;
    private Context context;
    private Resources resources;
    private AssetManager assetManager;

    @Before
    public void setUp() throws Exception {
        subject = new AndroidModule();
        context = PowerMockito.mock(Context.class);
        resources = PowerMockito.mock(Resources.class);
        assetManager = PowerMockito.mock(AssetManager.class);
    }

    @Test
    public void testShouldProvideContextFromApplication() throws Exception {
        Assert.assertThat(subject.provideAppContext(), CoreMatchers.equalTo(SoundWallApplication.getInstance().getApplicationContext()));
    }

    @Test
    public void testShouldProvideResourcesViaContext() throws Exception {
        Mockito.when(context.getResources()).thenReturn(resources);
        Assert.assertThat(subject.provideAppResources(context), CoreMatchers.equalTo(resources));
    }

    @Test
    public void testShouldProvideAssetManagerViaResources() throws Exception {
        Mockito.when(resources.getAssets()).thenReturn(assetManager);
        Assert.assertThat(subject.provideAssetManager(resources), CoreMatchers.equalTo(assetManager));
    }

    @Test
    public void testShouldProvideDefaultSharedPreferences() throws Exception {
        // Can't mock static methods without PowerMock
        Context context = subject.provideAppContext();
        Assert.assertThat(subject.provideSharedPreferences(context), CoreMatchers.instanceOf(SharedPreferences.class));
    }
}

package com.github.michaelengland.services;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class SoundWallWallpaperServiceTest {
    private SoundWallWallpaperService subject;

    @Before
    public void setUp() throws Exception {
        subject = new SoundWallWallpaperService();
        subject.onCreate();
    }

    @Test
    public void testShouldCreateNewEngines() throws Exception {
        Assert.assertThat(subject.onCreateEngine(), CoreMatchers.instanceOf(SoundWallWallpaperService
                .SoundWallWallpaperServiceEngine.class));
    }
}

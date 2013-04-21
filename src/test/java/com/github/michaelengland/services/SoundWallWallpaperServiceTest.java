package com.github.michaelengland.services;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

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
        assertThat(subject.onCreateEngine(), notNullValue());
    }
}

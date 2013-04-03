package com.github.michaelengland.modules;

import com.github.michaelengland.wallpaper.SoundWallArtistImpl;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class SoundWallModuleTest {
    private SoundWallModule subject;

    @Before
    public void setUp() throws Exception {
        subject = new SoundWallModule();
    }

    @Test
    public void testShouldProvideSoundWallArtist() throws Exception {
        Assert.assertThat(subject.provideSoundWallArtist(), CoreMatchers.instanceOf(SoundWallArtistImpl.class));
    }
}

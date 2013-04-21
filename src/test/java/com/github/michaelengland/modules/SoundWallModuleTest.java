package com.github.michaelengland.modules;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(RobolectricTestRunner.class)
public class SoundWallModuleTest {
    private SoundWallModule subject;

    @Before
    public void setUp() throws Exception {
        subject = new SoundWallModule();
    }

    @Test
    public void testShouldProvideSoundWallArtist() throws Exception {
        assertThat(subject.provideSoundWallArtist(), notNullValue());
    }

    @Test
    public void testShouldProvideBus() throws Exception {
        assertThat(subject.provideBus(), notNullValue());
    }
}

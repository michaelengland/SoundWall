package com.github.michaelengland.wallpaper;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore("Not sure how to meaningfully unit-test such a drawing implementation!")
@RunWith(RobolectricTestRunner.class)
public class SoundWallArtistImplTest {
    private SoundWallArtistImpl subject;

    @Before
    public void setUp() throws Exception {
        subject = new SoundWallArtistImpl();
    }
}

package com.github.michaelengland.services;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(RobolectricTestRunner.class)
public class SoundWallWallpaperDrawerTest {
    private SoundWallWallpaperService.SoundWallWallpaperDrawer subject;
    private SoundWallWallpaperService.SoundWallWallpaperServiceEngine engine;

    @Before
    public void setUp() throws Exception {
        engine = Mockito.mock(SoundWallWallpaperService.SoundWallWallpaperServiceEngine.class);
        subject = new SoundWallWallpaperService.SoundWallWallpaperDrawer(engine);
    }

    @Test
    public void testShouldCallEngineDrawer() throws Exception {
        subject.run();
        Mockito.verify(engine).draw();
    }
}

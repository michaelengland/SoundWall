package com.github.michaelengland.services;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class SoundWallWallpaperDrawerTest {
    private SoundWallWallpaperService.SoundWallWallpaperDrawer subject;
    @Mock
    private SoundWallWallpaperService.SoundWallWallpaperServiceEngine engine;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new SoundWallWallpaperService.SoundWallWallpaperDrawer(engine);
    }

    @Test
    public void testShouldCallEngineDrawer() throws Exception {
        subject.run();
        verify(engine).draw();
    }
}

package com.github.michaelengland.services;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;
import com.github.michaelengland.wallpaper.SoundWallArtist;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

@RunWith(RobolectricTestRunner.class)
public class SoundWallWallpaperServiceEngineTest {
    private SoundWallWallpaperService.SoundWallWallpaperServiceEngine subject;
    private SoundWallArtist artist;
    private Handler handler;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    @Before
    public void setUp() throws Exception {
        setupDependencies();
        setupSubject();
        setupSurfaceHolder();
        subject.onCreate(surfaceHolder);
    }

    private void setupDependencies() {
        handler = PowerMockito.mock(Handler.class);
        artist = PowerMockito.mock(SoundWallArtist.class);
    }

    private void setupSubject() {
        SoundWallWallpaperService service = new SoundWallWallpaperService();
        service.artist = artist;
        subject = PowerMockito.spy((SoundWallWallpaperService.SoundWallWallpaperServiceEngine) service
                .onCreateEngine());
        subject.wallpaperDrawingHandler = handler;
    }

    private void setupSurfaceHolder() {
        surfaceHolder = PowerMockito.mock(SurfaceHolder.class);
        canvas = new Canvas();
        PowerMockito.when(surfaceHolder.lockCanvas()).thenReturn(canvas);
        PowerMockito.when(subject.getSurfaceHolder()).thenReturn(surfaceHolder);
    }

    @Test
    public void testShouldDrawOnVisible() throws Exception {
        subject.onVisibilityChanged(true);
        verifyDraw();
    }

    @Test
    public void testShouldStopDrawingOnInvisible() throws Exception {
        subject.onVisibilityChanged(false);
        verifyStopDrawing();
    }

    @Test
    public void testShouldDrawOnSurfaceChange() throws Exception {
        subject.onSurfaceChanged(PowerMockito.mock(SurfaceHolder.class), 1, 1, 1);
        verifyDraw();
    }

    @Test
    public void testShouldStopDrawingOnSurfaceDestruction() throws Exception {
        subject.onSurfaceDestroyed(PowerMockito.mock(SurfaceHolder.class));
        verifyStopDrawing();
    }

    @Test
    public void testShouldDrawOnOffsetChange() throws Exception {
        subject.onOffsetsChanged(1.0f, 1.0f, 1.0f, 1.0f, 1, 1);
        verifyDraw();
    }

    @Test
    public void testShouldRestartDrawingAfterDrawingFinished() throws Exception {
        subject.visible = true;
        subject.draw();
        verifyStartDrawingNextFrame();
        verifyStopDrawing();
    }

    private void verifyStopDrawing() {
        Mockito.verify(handler).removeCallbacks(subject.wallpaperDrawer);
    }

    private void verifyStartDrawingNextFrame() {
        Mockito.verify(handler).postDelayed(subject.wallpaperDrawer, 20);
    }

    private void verifyDraw() {
        Mockito.verify(artist).draw(canvas);
    }
}

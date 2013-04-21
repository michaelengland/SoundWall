package com.github.michaelengland.services;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;
import com.github.michaelengland.wallpaper.SoundWallArtist;
import com.github.michaelengland.wallpaper.WallpaperState;
import com.github.michaelengland.wallpaper.WallpaperStateController;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Provider;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class SoundWallWallpaperServiceEngineTest {
    private SoundWallWallpaperService.SoundWallWallpaperServiceEngine subject;
    @Mock
    private SoundWallArtist artist;
    @Mock
    private WallpaperStateController controller;
    private Provider<SoundWallArtist> artistProvider;
    private Provider<WallpaperStateController> controllerProvider;
    @Mock
    private Handler handler;
    private Canvas canvas;
    @Mock
    private SurfaceHolder surfaceHolder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        setupDependencies();
        setupSubject();
        setupSurfaceHolder();
        subject.onCreate(surfaceHolder);
        subject.wallpaperDrawingHandler = handler;
    }

    private void setupDependencies() {
        artistProvider = new Provider<SoundWallArtist>() {
            @Override
            public SoundWallArtist get() {
                return artist;
            }
        };
        controllerProvider = new Provider<WallpaperStateController>() {
            @Override
            public WallpaperStateController get() {
                return controller;
            }
        };
    }

    private void setupSubject() {
        SoundWallWallpaperService service = new SoundWallWallpaperService();
        service.artistProvider = artistProvider;
        service.controllerProvider = controllerProvider;
        subject = spy((SoundWallWallpaperService.SoundWallWallpaperServiceEngine) service
                .onCreateEngine());
    }

    private void setupSurfaceHolder() {
        canvas = new Canvas();
        doReturn(canvas).when(surfaceHolder).lockCanvas();
        doReturn(surfaceHolder).when(subject).getSurfaceHolder();
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
        subject.onSurfaceChanged(mock(SurfaceHolder.class), 1, 1, 1);
        verifyDraw();
    }

    @Test
    public void testShouldStopDrawingOnSurfaceDestruction() throws Exception {
        subject.onSurfaceDestroyed(mock(SurfaceHolder.class));
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
        verify(handler).removeCallbacks(subject.wallpaperDrawer);
    }

    private void verifyStartDrawingNextFrame() {
        verify(handler).postDelayed(subject.wallpaperDrawer, 20);
    }

    private void verifyDraw() {
        verify(artist).draw(any(WallpaperState.class), eq(canvas));
    }
}

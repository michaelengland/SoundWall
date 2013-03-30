package com.github.michaelengland.activities;

import com.github.michaelengland.R;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class HelloAndroidActivityTest {
    private HelloAndroidActivity subject;

    @Before
    public void setUp() throws Exception {
        subject = new HelloAndroidActivity();
    }

    @Test
    public void testShouldMakeMeSmile() throws Exception {
        String appName = subject.getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("SoundWall"));
    }
}

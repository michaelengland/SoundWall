package com.github.michaelengland.activities;

import com.github.michaelengland.R;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        Assert.assertThat(appName, CoreMatchers.equalTo("SoundWall"));
    }
}

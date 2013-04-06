package com.github.michaelengland.activities;

import android.view.ViewGroup;
import butterknife.InjectView;
import butterknife.Views;
import com.github.michaelengland.R;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Ignore("Robolectric only supports v4 support library fragments")
@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {
    private SettingsActivity subject;
    @InjectView(R.id.settings)
    ViewGroup settingsFragment;

    @Before
    public void setUp() throws Exception {
        subject = new SettingsActivity();
        subject.onCreate(null);
        Views.inject(this, subject);
    }

    @Test
    public void testShouldShowSettingsFragment() throws Exception {
        Assert.assertNotNull(settingsFragment);
    }
}

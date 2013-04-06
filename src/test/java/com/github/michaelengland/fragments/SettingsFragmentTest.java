package com.github.michaelengland.fragments;

import android.content.Intent;
import android.preference.Preference;
import com.github.michaelengland.R;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import junit.framework.Assert;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;

@Ignore("Robolectric only supports v4 support library fragments")
@RunWith(RobolectricTestRunner.class)
public class SettingsFragmentTest {
    private SettingsFragment subject;

    @Before
    public void setUp() throws Exception {
        subject = new SettingsFragment();
        subject.onCreate(null);
    }

    @Test
    public void testShouldShowAccountCategory() throws Exception {
        Assert.assertNotNull(getPreferenceWithKeyId(R.string.login_key));
    }

    @Ignore("Impossible to performClick on a preference")
    @Test
    public void testShouldLaunchSoundWallWebPageOnClick() throws Exception {
        // getPreferenceWithKeyId(R.string.sound_wall).performClick();
        assertOpenedUriString("https://github.com/michaelengland/SoundWall");
    }

    @Ignore("Impossible to performClick on a preference")
    @Test
    public void testShouldLaunchSoundCloudWebPageOnClick() throws Exception {
        // getPreferenceWithKeyId(R.string.sound_cloud).performClick();
        assertOpenedUriString("https://soundcloud.com");
    }

    @Test
    public void testShouldShowAboutCategory() throws Exception {
        Assert.assertNotNull(getPreferenceWithKeyId(R.string.sound_wall_key));
        Assert.assertNotNull(getPreferenceWithKeyId(R.string.sound_cloud_key));
    }

    private Preference getPreferenceWithKeyId(int resId) {
        return subject.getPreferenceManager().findPreference(subject.getString(resId));
    }

    private void assertOpenedUriString(String urlString) {
        ShadowActivity shadowActivity = Robolectric.shadowOf(subject.getActivity());
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);
        assertThat(shadowIntent.getAction(), CoreMatchers.equalTo(Intent.ACTION_VIEW));
        assertThat(shadowIntent.getData().toString(), CoreMatchers.equalTo(urlString));
    }
}

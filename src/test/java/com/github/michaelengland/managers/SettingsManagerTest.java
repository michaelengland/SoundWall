package com.github.michaelengland.managers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.soundcloud.api.Token;
import com.squareup.otto.Bus;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

@RunWith(RobolectricTestRunner.class)
public class SettingsManagerTest {
    private SettingsManager subject;
    private SharedPreferences sharedPreferences;
    private Token token;
    private Bus bus;

    @Before
    public void setUp() throws Exception {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Robolectric.application);
        bus = PowerMockito.mock(Bus.class);
        subject = new SettingsManager(sharedPreferences, bus);
        token = new Token("access", "refresh", "scope");
    }

    @Test
    public void testShouldRegisterWithBus() throws Exception {
        Mockito.verify(bus).register(subject);
    }

    @Test
    public void testShouldRetrieveTokenFromSharedPreferences() throws Exception {
        sharedPreferences.edit().putString("token_access", "access").putString("token_refresh",
                "refresh").putString("token_scope", "scope").commit();
        Assert.assertThat(subject.getToken(), CoreMatchers.equalTo(token));
    }

    @Test
    public void testShouldStoreTokenToSharedPreferences() throws Exception {
        subject.setToken(token);
        Assert.assertThat(sharedPreferences.getString("token_access", null), CoreMatchers.equalTo("access"));
        Assert.assertThat(sharedPreferences.getString("token_refresh", null), CoreMatchers.equalTo("refresh"));
        Assert.assertThat(sharedPreferences.getString("token_scope", null), CoreMatchers.equalTo("scope"));
    }

    @Test
    public void testShouldRemoveSharedPreferencesWhenTokenSetToNull() throws Exception {
        sharedPreferences.edit().putString("token_access", "access").putString("token_refresh",
                "refresh").putString("token_scope", "scope").commit();
        subject.setToken(null);
        Assert.assertNull(sharedPreferences.getString("token_access", null));
        Assert.assertNull(sharedPreferences.getString("token_refresh", null));
        Assert.assertNull(sharedPreferences.getString("token_scope", null));
    }

    @Test
    public void testShouldFireTokenChangeEventWhenSettingToken() throws Exception {
        subject.setToken(token);
        Mockito.verify(bus).post(new TokenChangeEvent());
    }
}

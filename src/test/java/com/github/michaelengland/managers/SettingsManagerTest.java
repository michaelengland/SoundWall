package com.github.michaelengland.managers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.soundcloud.api.Token;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(RobolectricTestRunner.class)
public class SettingsManagerTest {
    private SettingsManager subject;
    private SharedPreferences sharedPreferences;
    private Token token;

    @Before
    public void setUp() throws Exception {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Robolectric.application);
        subject = new SettingsManager(sharedPreferences);
        token = new Token("access", "refresh", "scope");
    }

    @Test
    public void testShouldRetrieveTokenFromSharedPreferences() throws Exception {
        sharedPreferences.edit().putString("token_access", "access").putString("token_refresh",
                "refresh").putString("token_scope", "scope").commit();
        assertThat(subject.getToken(), equalTo(token));
    }

    @Test
    public void testShouldStoreTokenToSharedPreferences() throws Exception {
        subject.setToken(token);
        assertThat(sharedPreferences.getString("token_access", null), equalTo("access"));
        assertThat(sharedPreferences.getString("token_refresh", null), equalTo("refresh"));
        assertThat(sharedPreferences.getString("token_scope", null), equalTo("scope"));
    }

    @Test
    public void testShouldRemoveSharedPreferencesWhenTokenSetToNull() throws Exception {
        sharedPreferences.edit().putString("token_access", "access").putString("token_refresh",
                "refresh").putString("token_scope", "scope").commit();
        subject.setToken(null);
        assertThat(sharedPreferences.getString("token_access", null), nullValue());
        assertThat(sharedPreferences.getString("token_refresh", null), nullValue());
        assertThat(sharedPreferences.getString("token_scope", null), nullValue());
    }
}

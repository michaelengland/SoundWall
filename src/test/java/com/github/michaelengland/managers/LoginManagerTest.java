package com.github.michaelengland.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import com.soundcloud.api.Token;
import com.squareup.otto.Bus;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

@RunWith(RobolectricTestRunner.class)
public class LoginManagerTest {
    private LoginManager subject;
    private SettingsManager settingsManager;
    private AccountManager accountManager;
    private Token token;
    private Bus bus;
    private Account[] accounts;

    @Before
    public void setUp() throws Exception {
        settingsManager = PowerMockito.mock(SettingsManager.class);
        accountManager = PowerMockito.mock(AccountManager.class);
        bus = PowerMockito.mock(Bus.class);
        token = new Token("", "");
        subject = new LoginManager(accountManager, settingsManager, bus);
        accounts = new Account[2];
        accounts[0] = new Account("testUser", "com.soundcloud.android.account");
        accounts[1] = new Account("otherUser", "com.soundcloud.android.account");
    }

    @Test
    public void testShouldRegisterWithBus() throws Exception {
        Mockito.verify(bus).register(subject);
    }

    @Test
    public void testShouldBeLoggedInWhenTokenExists() throws Exception {
        Mockito.when(settingsManager.getToken()).thenReturn(token);
        Assert.assertTrue(subject.isLoggedIn());
    }

    @Test
    public void testShouldBeLoggedOutWhenTokenDoesntExist() throws Exception {
        Mockito.when(settingsManager.getToken()).thenReturn(null);
        Assert.assertFalse(subject.isLoggedIn());
    }

    @Test
    public void testShouldNotReturnSoundCloudAccountWhenNoSoundCloudAccountsSaved() throws Exception {
        Mockito.when(accountManager.getAccountsByType("com.soundcloud.android.account")).thenReturn(new Account[0]);
        Assert.assertNull(subject.soundCloudAccount());
    }

    @Test
    public void testShouldReturnSoundCloudAccountWhenMultipleSoundCloudAccountsSaved() throws Exception {
        Mockito.when(accountManager.getAccountsByType("com.soundcloud.android.account")).thenReturn(accounts);
        Assert.assertThat(subject.soundCloudAccount(), CoreMatchers.equalTo(accounts[0]));
    }

    @Test
    public void testShouldNotSuggestedAnyUsernameWhenNoSoundCloudAccountsSaved() throws Exception {
        Mockito.when(accountManager.getAccountsByType("com.soundcloud.android.account")).thenReturn(new Account[0]);
        Assert.assertNull(subject.suggestedUsername());
    }

    @Test
    public void testShouldSuggestFirstUsernameWhenMultipleSoundCloudAccountsSaved() throws Exception {
        Mockito.when(accountManager.getAccountsByType("com.soundcloud.android.account")).thenReturn(accounts);
        Assert.assertThat(subject.suggestedUsername(), CoreMatchers.equalTo("testUser"));
    }

    @Test
    public void testShouldSetTokenOnLogin() throws Exception {
        subject.login(token);
        Mockito.verify(settingsManager).setToken(token);
    }

    @Test
    public void testShouldFireUserStateChangeEventOnLogin() throws Exception {
        subject.login(token);
        Mockito.verify(bus).post(new UserStateChangeEvent());
    }

    @Test
    public void testShouldSetTokenToNullOnLogout() throws Exception {
        subject.logout();
        Mockito.verify(settingsManager).setToken(null);
    }

    @Test
    public void testShouldFireUserStateChangeEventOnLogout() throws Exception {
        subject.logout();
        Mockito.verify(bus).post(new UserStateChangeEvent());
    }
}

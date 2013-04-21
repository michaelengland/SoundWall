package com.github.michaelengland.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import com.soundcloud.api.Token;
import com.squareup.otto.Bus;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class LoginManagerTest {
    private LoginManager subject;
    @Mock
    private SettingsManager settingsManager;
    @Mock
    private AccountManager accountManager;
    private Token token;
    @Mock
    private Bus bus;
    private Account[] accounts;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        token = new Token("", "");
        subject = new LoginManager(accountManager, settingsManager, bus);
        accounts = new Account[2];
        accounts[0] = new Account("testUser", "com.soundcloud.android.account");
        accounts[1] = new Account("otherUser", "com.soundcloud.android.account");
    }

    @Test
    public void testShouldRegisterWithBus() throws Exception {
        verify(bus).register(subject);
    }

    @Test
    public void testShouldBeLoggedInWhenTokenExists() throws Exception {
        doReturn(token).when(settingsManager).getToken();
        assertThat(subject.isLoggedIn(), is(true));
    }

    @Test
    public void testShouldBeLoggedOutWhenTokenDoesntExist() throws Exception {
        doReturn(null).when(settingsManager).getToken();
        assertThat(subject.isLoggedIn(), is(false));
    }

    @Test
    public void testShouldNotReturnSoundCloudAccountWhenNoSoundCloudAccountsSaved() throws Exception {
        doReturn(new Account[0]).when(accountManager).getAccountsByType("com.soundcloud.android.account");
        assertThat(subject.soundCloudAccount(), nullValue());
    }

    @Test
    public void testShouldReturnSoundCloudAccountWhenMultipleSoundCloudAccountsSaved() throws Exception {
        doReturn(accounts).when(accountManager).getAccountsByType("com.soundcloud.android.account");
        assertThat(subject.soundCloudAccount(), equalTo(accounts[0]));
    }

    @Test
    public void testShouldNotSuggestedAnyUsernameWhenNoSoundCloudAccountsSaved() throws Exception {
        doReturn(new Account[0]).when(accountManager).getAccountsByType("com.soundcloud.android.account");
        assertThat(subject.suggestedUsername(), nullValue());
    }

    @Test
    public void testShouldSuggestFirstUsernameWhenMultipleSoundCloudAccountsSaved() throws Exception {
        doReturn(accounts).when(accountManager).getAccountsByType("com.soundcloud.android.account");
        assertThat(subject.suggestedUsername(), equalTo("testUser"));
    }

    @Test
    public void testShouldSetTokenOnLogin() throws Exception {
        subject.login(token);
        verify(settingsManager).setToken(token);
    }

    @Test
    public void testShouldFireUserStateChangeEventOnLogin() throws Exception {
        subject.login(token);
        verify(bus).post(new UserStateChangeEvent());
    }

    @Test
    public void testShouldSetTokenToNullOnLogout() throws Exception {
        subject.logout();
        verify(settingsManager).setToken(null);
    }

    @Test
    public void testShouldFireUserStateChangeEventOnLogout() throws Exception {
        subject.logout();
        verify(bus).post(new UserStateChangeEvent());
    }
}

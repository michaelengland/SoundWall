package com.github.michaelengland.api;

import com.github.michaelengland.managers.SettingsManager;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Token;

import javax.inject.Inject;
import java.io.IOException;

public class SoundCloudClient {
    ApiWrapper apiWrapper;
    SettingsManager settingsManager;

    @Inject
    SoundCloudClient(final ApiWrapper apiWrapper, final SettingsManager settingsManager) {
        this.apiWrapper = apiWrapper;
        this.settingsManager = settingsManager;
    }

    public Token login(String username, String password) throws SoundCloudClientException {
        try {
            return performLogin(username, password);
        } catch (IOException e) {
            throw new SoundCloudClientException(e);
        }
    }

    private Token performLogin(String username, String password) throws IOException {
        Token token = apiWrapper.login(username, password);
        settingsManager.setToken(token);
        return token;
    }
}

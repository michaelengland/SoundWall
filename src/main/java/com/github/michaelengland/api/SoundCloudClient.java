package com.github.michaelengland.api;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Token;

import javax.inject.Inject;
import java.io.IOException;

public class SoundCloudClient {
    ApiWrapper apiWrapper;

    @Inject
    SoundCloudClient(final ApiWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    public Token login(String username, String password) throws SoundCloudClientException {
        try {
            return performLogin(username, password);
        } catch (IOException e) {
            throw new SoundCloudClientException(e);
        }
    }

    private Token performLogin(String username, String password) throws IOException {
        return apiWrapper.login(username, password);
    }
}

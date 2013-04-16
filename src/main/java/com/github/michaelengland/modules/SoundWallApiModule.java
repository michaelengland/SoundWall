package com.github.michaelengland.modules;

import android.content.res.AssetManager;
import com.github.michaelengland.api.TracksParser;
import com.github.michaelengland.managers.SettingsManager;
import com.soundcloud.api.ApiWrapper;
import dagger.Module;
import dagger.Provides;

import java.io.IOException;
import java.util.Properties;

@Module(
        complete = false
)
public class SoundWallApiModule {
    private static final String SOUND_CLOUD_PROPERTIES_FILE = "soundcloud.properties";
    private static final String SOUND_CLOUD_CLIENT_ID_KEY = "soundcloud.client_id";
    private static final String SOUND_CLOUD_CLIENT_SECRET_KEY = "soundcloud.client_secret";

    @Provides
    ApiWrapper provideApiWrapper(final AssetManager assetManager, final SettingsManager settingsManager) {
        Properties apiProperties = getApiProperties(assetManager);
        String clientId = apiProperties.getProperty(SOUND_CLOUD_CLIENT_ID_KEY);
        String clientSecret = apiProperties.getProperty(SOUND_CLOUD_CLIENT_SECRET_KEY);
        if (clientId != null && clientSecret != null) {
            return new ApiWrapper(clientId, clientSecret, null, settingsManager.getToken());
        } else {
            return null;
        }
    }

    private Properties getApiProperties(final AssetManager assetManager) {
        try {
            Properties properties = new Properties();
            properties.load(assetManager.open(SOUND_CLOUD_PROPERTIES_FILE));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("You must set a soundcloud.properties file in the assets directory. Check " +
                    "soundcloud.properties.example for an example");
        }
    }

    @Provides
    TracksParser provideTracksParser() {
        return new TracksParser();
    }
}

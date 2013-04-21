package com.github.michaelengland.entities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Decorator to wrap around URL class. Allows mocking.
 */
public class Url {
    private URL url;

    public Url(String urlString) throws MalformedURLException {
        this.url = new URL(urlString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Url url1 = (Url) o;

        if (url != null ? !url.equals(url1.url) : url1.url != null) return false;

        return true;
    }

    public URLConnection openConnection() throws IOException {
        return url.openConnection();
    }
}

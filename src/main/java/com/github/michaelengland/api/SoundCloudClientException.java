package com.github.michaelengland.api;

public class SoundCloudClientException extends Exception {
    public SoundCloudClientException(Exception e) {
        super(e.getMessage());
    }
}

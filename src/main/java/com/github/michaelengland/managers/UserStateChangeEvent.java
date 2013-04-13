package com.github.michaelengland.managers;

public class UserStateChangeEvent {
    @Override
    public boolean equals(Object o) {
        return o instanceof UserStateChangeEvent;
    }
}

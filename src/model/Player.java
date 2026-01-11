package model;

import java.util.Objects;

public final class Player {
    private final String name;

    public Player(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    @Override public String toString() {
        return name;
    }
}
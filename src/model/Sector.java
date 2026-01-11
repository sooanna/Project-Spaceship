package model;

import java.util.Objects;

public final class Sector {
    private final String name;

    public Sector(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sector other)) return false;
        return name.equals(other.name);
    }
    @Override public int hashCode() { return name.hashCode(); }

}
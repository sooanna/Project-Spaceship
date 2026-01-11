package model;

import rules.StarbaseRule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Starbase implements Combatant {

    public static final int DEFAULT_MAX_HEALTH = 500;
    public static final int DEFAULT_MAX_DEFENCE = 20;

    private final String name;
    private final Sector sector;
    private final Fleet fleet;

    private final int maxHealth = DEFAULT_MAX_HEALTH;
    private final int maxDefenceStrength = DEFAULT_MAX_DEFENCE;

    private int currentHealth = maxHealth;
    private final List<Starship> dockedShips = new ArrayList<>();

    public Starbase(String name, Sector sector, Fleet fleet) {
        this.name = Objects.requireNonNull(name);
        this.sector = Objects.requireNonNull(sector);
        this.fleet = Objects.requireNonNull(fleet);
    }

    @Override public String getName() { return name; }
    @Override public Fleet getFleet() { return fleet; }
    @Override public Sector getSector() { return sector; }

    @Override public boolean isActive() { return currentHealth > 0; }
    @Override public int getMaxHealth() { return maxHealth; }
    @Override public int getCurrentHealth() { return currentHealth; }

    public int getMaxDefenceStrength() { return maxDefenceStrength; }

    public List<Starship> getDockedShips() {
        return Collections.unmodifiableList(dockedShips);
    }

    void addDockedShip(Starship s) { dockedShips.add(s); }
    void removeDockedShip(Starship s) { dockedShips.remove(s); }

    @Override
    public int getCurrentDefenceStrength() {
        return StarbaseRule.currentDefenceStrength(maxDefenceStrength, currentHealth, maxHealth, dockedShips);
    }

    @Override
    public void takeDamage(int damage) {
        if (!isActive()) return;
        if (damage <= 0) return;

        currentHealth = Math.max(0, currentHealth - damage);

        // When starbase reaches 0, all docked ships are also disabled
        if (currentHealth == 0) {
            for (Starship s : new ArrayList<>(dockedShips)) {
                s.disableDueToStarbaseDestruction();
            }
            dockedShips.clear();
        }
    }

    // used just for logging in the App.java
    public List<String> getDockedShipNames() {
        List<String> names = new ArrayList<>();
        for (Starship s : dockedShips) names.add(s.getName());
        return names;
    }
}

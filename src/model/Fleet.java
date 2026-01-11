package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Fleet {
    private final Player player;
    private final List<Starship> starships = new ArrayList<>();
    private final List<Starbase> starbases = new ArrayList<>();

    public Fleet(Player player) {
        this.player = Objects.requireNonNull(player);
    }

    public Player getPlayer() {
        return player;
    }

    public void addStarship(Starship s) {
        starships.add(Objects.requireNonNull(s));
    }

    public void addStarbase(Starbase b) {
        starbases.add(Objects.requireNonNull(b));
    }

    public List<Starship> getActiveStarships() {
        List<Starship> active = new ArrayList<>();
        for (Starship s : starships) {
            if (s.isActive()) active.add(s);
        }
        return active;
    }

    public List<Starbase> getActiveStarbases() {
        List<Starbase> active = new ArrayList<>();
        for (Starbase b : starbases) {
            if (b.isActive()) active.add(b);
        }
        return active;
    }

    // Mobilise moves all ships correctly
    public void mobiliseTo(Sector sector) {
        for (Starship s : getActiveStarships()) {
            if (!s.isDocked()) {
                s.moveTo(sector);
            }
        }
    }

    // each non-docked ship in same sector attacks the target
    public void attackTarget(Combatant target) {
        if (target == null || !target.isActive()) return;

        List<Starship> attackers = getActiveStarships();
        for (Starship s : attackers) {
            // when target is destroyed, then no attacks should happen
            if (!target.isActive()) break;

            if (!s.isDocked() && s.getSector().equals(target.getSector())) {
                s.attack(target);
            }
        }
    }
}
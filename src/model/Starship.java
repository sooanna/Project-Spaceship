package model;

import rules.Damage;
import rules.Repair;
import rules.StarshipRule;

import java.util.Objects;

public final class Starship implements Combatant {

    public static final int DEFAULT_MAX_CREW = 10;
    public static final int DEFAULT_MAX_HEALTH = 100;
    public static final int DEFAULT_MAX_ATTACK = 30;
    public static final int DEFAULT_MAX_DEFENCE = 10;

    private final String name;
    private final Fleet fleet;

    private Sector sector;
    private Starbase dockedIn;

    private final int maxCrew = DEFAULT_MAX_CREW;
    private final int maxHealth = DEFAULT_MAX_HEALTH;
    private final int maxAttackStrength = DEFAULT_MAX_ATTACK;
    private final int maxDefenceStrength = DEFAULT_MAX_DEFENCE;

    private int currentCrew = maxCrew;
    private int currentHealth = maxHealth;

    // next X attempted actions are skipped (repair)
    private int skippedActions = 0;

    public Starship(String name, Sector sector, Fleet fleet) {
        this.name = Objects.requireNonNull(name);
        this.sector = Objects.requireNonNull(sector);
        this.fleet = Objects.requireNonNull(fleet);
    }

    @Override public String getName() { return name; }
    @Override public Fleet getFleet() { return fleet; }
    @Override public Sector getSector() { return sector; }

    public boolean isDocked() { return dockedIn != null; }
    @Override public boolean isActive() { return currentHealth > 0; }

    @Override public int getMaxHealth() { return maxHealth; }
    @Override public int getCurrentHealth() { return currentHealth; }

    public int getMaxCrew() { return maxCrew; }
    public int getCurrentCrew() { return currentCrew; }

    public int getCurrentAttackStrength() {
        return StarshipRule.currentAttackStrength(maxAttackStrength, currentHealth, maxHealth);
    }

    @Override
    public int getCurrentDefenceStrength() {
        return StarshipRule.currentDefenceStrength(maxDefenceStrength, currentHealth, maxHealth, currentCrew, maxCrew);
    }

    public void moveTo(Sector newSector) {
        if (skipIfRepairing()) return;
        if (!isActive()) return;
        if (isDocked()) return;

        sector = Objects.requireNonNull(newSector);
    }

    public void dock(Starbase starbase) {
        if (skipIfRepairing()) return;
        if (!isActive()) return;

        Objects.requireNonNull(starbase);

        if (isDocked()) return;
        if (!starbase.isActive()) return;
        if (!fleet.equals(starbase.getFleet())) return;
        if (!sector.equals(starbase.getSector())) return;

        dockedIn = starbase;
        starbase.addDockedShip(this);
    }

    public void undock() {
        if (skipIfRepairing()) return;
        if (!isActive()) return;
        if (!isDocked()) return;

        Starbase base = dockedIn;
        dockedIn = null;
        base.removeDockedShip(this);
    }

    public void repair() {
        if (skipIfRepairing()) return;
        if (!isActive()) return;
        if (!isDocked()) return;

        skippedActions = Repair.actionsToSkipAfterRepair(currentHealth, maxHealth);
        currentHealth = maxHealth;
        currentCrew = maxCrew;
    }

    public void attack(Combatant target) {
        if (skipIfRepairing()) return;
        if (!isActive()) return;

        Objects.requireNonNull(target);

        if (isDocked()) return;
        if (!target.isActive()) return;
        if (target.getFleet().equals(fleet)) return;
        if (!sector.equals(target.getSector())) return;

        // Docked starships cannot be attacked
        if (target instanceof Starship shipTarget && shipTarget.isDocked()) return;

        int attackerAS = getCurrentAttackStrength();
        int targetDef = target.getCurrentDefenceStrength();
        int damage = Damage.calculateDamage(attackerAS, targetDef);

        target.takeDamage(damage);

        // Crew incapacitation: only for starship targets and only if still active after hit
        if (damage > 0 && target instanceof Starship shipTarget && shipTarget.isActive()) {
            int incapacitated = Damage.calculateIncapacitatedCrew(damage, shipTarget.getMaxHealth(), shipTarget.getCurrentCrew());
            shipTarget.applyCrewLoss(incapacitated);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (!isActive()) return;
        if (damage <= 0) return;

        currentHealth = Math.max(0, currentHealth - damage);

        if (currentHealth == 0 && isDocked()) {
            Starbase base = dockedIn;
            dockedIn = null;
            base.removeDockedShip(this);
        }
    }

    // Called by Starbase when it is destroyed
    void disableDueToStarbaseDestruction() {
        if (isDocked()) {
            Starbase base = dockedIn;
            dockedIn = null;
            base.removeDockedShip(this);
        }
        currentHealth = 0;
    }

    private int applyCrewLoss(int incapacitated) {
        int before = currentCrew;
        int loss = Math.max(0, incapacitated);
        currentCrew = Math.max(1, currentCrew - loss);
        return before - currentCrew;
    }

    private boolean skipIfRepairing() {
        if (skippedActions > 0) {
            skippedActions--;
            return true;
        }
        return false;
    }
}

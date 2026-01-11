package rules;

public final class Repair {
    private Repair() {}

    public static int actionsToSkipAfterRepair(int currentHealth, int maxHealth) {
        double pct = (double) currentHealth / (double) maxHealth;

        if (pct < 0.25) return 4;
        if (pct < 0.50) return 3;
        if (pct < 0.75) return 2;
        return 1;
    }
}
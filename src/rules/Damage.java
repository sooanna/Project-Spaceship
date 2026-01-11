package rules;

public final class Damage {
    private Damage() {}

    public static int calculateDamage(int attackerCurrentAttack, int targetCurrentDefence) {
        return Math.max(0, attackerCurrentAttack - targetCurrentDefence);
    }

    public static int calculateIncapacitatedCrew(int damage, int targetMaxHealth, int targetCurrentCrew) {
        if (damage <= 0) return 0;
        double raw = ((double) damage / (double) targetMaxHealth) * (double) targetCurrentCrew;
        return (int) Math.floor(raw);
    }
}
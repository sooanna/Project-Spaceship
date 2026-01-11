package rules;

public final class StarshipRule {
    private StarshipRule() {}

    // ceil(MaxAttack × (CurrentHealth / MaxHealth))
    public static int currentAttackStrength(int maxAttack, int currentHealth, int maxHealth) {
        double ratio = (double) currentHealth / (double) maxHealth;
        return (int) Math.ceil(maxAttack * ratio);
    }

    // floor(MaxDefence × ((CurrentHealth + CurrentCrew) / (MaxHealth + MaxCrew)))
    public static int currentDefenceStrength(int maxDef, int currentHealth, int maxHealth, int currentCrew, int maxCrew) {
        double numerator = (double) currentHealth + (double) currentCrew;
        double denominator = (double) maxHealth + (double) maxCrew;
        return (int) Math.floor(maxDef * (numerator / denominator));
    }
}

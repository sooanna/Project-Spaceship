package rules;

import model.Starship;

import java.util.List;

public final class StarbaseRule {
    private StarbaseRule() {}

    // floor( MaxDef * (CurrentHealth/MaxHealth) + (SumDockedDef) * (NumDocked / MaxDef) )
    public static int currentDefenceStrength(int maxDef, int currentHealth, int maxHealth, List<Starship> dockedShips) {
        double basePart = (double) maxDef * ((double) currentHealth / (double) maxHealth);

        int numDocked = 0;
        int sumDockedDef = 0;

        for (Starship s : dockedShips) {
            if (s.isActive()) {
                numDocked++;
                sumDockedDef += s.getCurrentDefenceStrength();
            }
        }

        double dockedPart = 0.0;
        if (numDocked > 0) {
            dockedPart = (double) sumDockedDef * ((double) numDocked / (double) maxDef);
        }

        return (int) Math.floor(basePart + dockedPart);
    }
}

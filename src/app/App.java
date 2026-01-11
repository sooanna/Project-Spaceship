package app;

import model.*;

import java.util.List;

public final class App {

    public static void main(String[] args) {

        Sector sector1 = new Sector("Sector 1");
        Sector sector2 = new Sector("Sector 2");

        Player p1 = new Player("Player 1");
        Player p2 = new Player("Player 2");

        System.out.println("Creates Player 1 fleet with 1 starbase and 3 ships in Sector 1");
        Fleet fleet1 = new Fleet(p1);
        Starbase p1Base = new Starbase("Player1 Starbase", sector1, fleet1);
        fleet1.addStarbase(p1Base);

        Starship player1ShipA = new Starship("Ship A (P1)", sector1, fleet1);
        Starship player1ShipB = new Starship("Ship B (P1)", sector1, fleet1);
        Starship player1ShipC = new Starship("Ship C (P1)", sector1, fleet1);
        fleet1.addStarship(player1ShipA);
        fleet1.addStarship(player1ShipB);
        fleet1.addStarship(player1ShipC);

        System.out.println("Creates Player 2 fleet with 1 starbase and 3 ships in Sector 2");
        Fleet fleet2 = new Fleet(p2);
        Starbase p2Base = new Starbase("Player2 Starbase", sector2, fleet2);
        fleet2.addStarbase(p2Base);

        Starship player2ShipA = new Starship("Ship A (P2)", sector2, fleet2);
        Starship player2ShipB = new Starship("Ship B (P2)", sector2, fleet2);
        Starship player2ShipC = new Starship("Ship C (P2)", sector2, fleet2);
        fleet2.addStarship(player2ShipA);
        fleet2.addStarship(player2ShipB);
        fleet2.addStarship(player2ShipC);

        System.out.println("\nMoves all Player 1 ships to Sector 2");
        fleet1.mobiliseTo(sector2);

        System.out.println("\nDocks two Player 2 ships into Player 2 starbase");
        player2ShipA.dock(p2Base);
        player2ShipB.dock(p2Base);

        System.out.println("\nSelects one Player 1 ship and attacks remaining undocked Player 2 ship two times");
        logAttack(player1ShipA, player2ShipC);
        logAttack(player1ShipA, player2ShipC);

        System.out.println("\nDocks remaining undocked Player 2 ship then repairs it");
        player2ShipC.dock(p2Base);

        int healthBeforeRepair = player2ShipC.getCurrentHealth();
        player2ShipC.repair();
        System.out.println("Repair: " + player2ShipC.getName()
                + " health " + healthBeforeRepair + " -> " + player2ShipC.getCurrentHealth());

        System.out.println("\nCommands all Player 1 ships to attack Player 2 starbase until destroyed");

        int rounds = 0;
        while (p2Base.isActive()) {
            rounds++;

            System.out.println("\n--- Round " + rounds + " ---");

            List<String> dockedBefore = p2Base.getDockedShipNames();

            logAttack(player1ShipA, p2Base);
            if (!p2Base.isActive()) {
                printStarbaseDestroyed(p2Base, dockedBefore);
                break;
            }

            logAttack(player1ShipB, p2Base);
            if (!p2Base.isActive()) {
                printStarbaseDestroyed(p2Base, dockedBefore);
                break;
            }

            logAttack(player1ShipC, p2Base);
            if (!p2Base.isActive()) {
                printStarbaseDestroyed(p2Base, dockedBefore);
                break;
            }
        }

        System.out.println("\nEnd:");
        System.out.println(p2Base.isActive()
                ? p2Base.getName() + " is still active"
                : p2Base.getName() + " is not active");

        System.out.println("Player 2 active ships: " + fleet2.getActiveStarships().size());
        System.out.println("Player 2 active bases: " + fleet2.getActiveStarbases().size());
    }

    private static void logAttack(Starship attacker, Combatant target) {
        int hpBefore = target.getCurrentHealth();
        int defBefore = target.getCurrentDefenceStrength();

        attacker.attack(target);

        int hpAfter = target.getCurrentHealth();
        int damage = hpBefore - hpAfter;

        if (damage <= 0) {
            System.out.println(attacker.getName() + " attacks " + target.getName() + " → no effect"
                    + " (defence: " + defBefore + ", health: " + hpBefore + ")");
            return;
        }

        int defAfter = target.isActive() ? target.getCurrentDefenceStrength() : 0;

        System.out.println(attacker.getName() + " attacks " + target.getName()
                + " → damage " + damage
                + " (health: " + hpBefore + " → " + hpAfter
                + ", defence: " + defBefore + " → " + defAfter + ")");
    }

    private static void printStarbaseDestroyed(Starbase base, List<String> dockedBefore) {
        System.out.println(base.getName() + " DESTROYED");
        if (dockedBefore != null && !dockedBefore.isEmpty()) {
            System.out.println("Docked ships disabled:");
            for (String s : dockedBefore) {
                System.out.println("  - " + s);
            }
        }
    }
}

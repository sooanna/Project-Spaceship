package model;

public interface Combatant {
    String getName();
    Fleet getFleet();
    Sector getSector();

    boolean isActive();
    int getMaxHealth();
    int getCurrentHealth();
    int getCurrentDefenceStrength();
    void takeDamage(int damage);
}
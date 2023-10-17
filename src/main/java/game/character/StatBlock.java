package game.character;

import lombok.Value;
import lombok.With;

@Value
@With
public class StatBlock {
    // Core Stats
    int strength = 1;
    int agility = 2;
    int intelligence = 1;

    // Secondary Stats
    int health = strength * 10;
    int defense = strength * 2;
    int critical = strength * 2;
    int accuracy = intelligence * 2;
    int dodge = agility * 2;
    int speed = agility * 2;
    int physical = strength * 2;
    int magical = intelligence * 2;
    int initiative = agility * 2;
    int cooldown = intelligence * 2;
    int range = intelligence * 2;
}

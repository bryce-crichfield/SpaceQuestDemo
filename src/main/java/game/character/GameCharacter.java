package game.character;

import lombok.Data;

import java.util.Set;

@Data
public class GameCharacter {
    private String name = "Star Trooper";
    private Level level;
    private StatBlock baseStats;
    private Set<StatModifier> modifiers;
    private StatBlock cachedStats;

    public GameCharacter(Level level, StatBlock baseStats, Set<StatModifier> modifiers) {
        this.level = level;
        this.baseStats = baseStats;
        this.modifiers = modifiers;
        this.cachedStats = calculateStats();
    }

    private StatBlock calculateStats() {
        StatBlock stats = baseStats;
        for (StatModifier modifier : modifiers) {
            stats = modifier.apply(stats);
        }

        return stats;
    }

    public StatBlock getStats() {
        return cachedStats;
    }
}

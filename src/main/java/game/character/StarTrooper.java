package game.character;

import java.util.Set;

public class StarTrooper extends GameCharacter {
    public StarTrooper(Level level, StatBlock baseStats, Set<StatModifier> modifiers) {
        super(level, baseStats, modifiers);
    }

    public static StarTrooper create() {
        Level level = new Level(1);
        StatBlock baseStats = new StatBlock();
        return new StarTrooper(level, baseStats, Set.of());
    }
}

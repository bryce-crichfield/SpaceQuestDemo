package game.state.battle.event;

import game.event.Event;
import game.state.battle.world.Actor;
import game.state.battle.world.Tile;

import java.util.List;

public class ActorAttacked {
    public static final Event<ActorAttacked> event = new Event<>();

    public Actor getAttacker() {
        return attacker;
    }

    public List<Tile> getTargets() {
        return targets;
    }

    Actor attacker;
    List<Tile> targets;

    public ActorAttacked(Actor attacker, List<Tile> targets) {
        this.attacker = attacker;
        this.targets = targets;
    }
}

package game.state.battle.event;

import game.event.Event;
import game.state.battle.model.Actor;
import game.state.battle.model.Tile;

import java.util.List;

public class ActionActorAttack {
    public static final Event<ActionActorAttack> event = new Event<>();
    Actor attacker;
    List<Tile> targets;

    public ActionActorAttack(Actor attacker, List<Tile> targets) {
        this.attacker = attacker;
        this.targets = targets;
    }

    public Actor getAttacker() {
        return attacker;
    }

    public List<Tile> getTargets() {
        return targets;
    }
}

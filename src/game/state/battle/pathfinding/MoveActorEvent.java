package game.state.battle.pathfinding;

import game.state.battle.world.Actor;
import game.state.battle.world.Tile;

import java.util.List;

public class MoveActorEvent {
    public final Actor actor;
    public final List<Tile> movePath;

    public MoveActorEvent(Actor actor, List<Tile> movePath) {
        this.actor = actor;
        this.movePath = movePath;
    }
}

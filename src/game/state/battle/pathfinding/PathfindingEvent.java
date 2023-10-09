package game.state.battle.pathfinding;

import game.state.battle.world.Actor;
import game.state.battle.world.Tile;

import java.util.List;

public class PathfindingEvent {
    public final Actor actor;
    public final List<Tile> movePath;

    public PathfindingEvent(Actor actor, List<Tile> movePath) {
        this.actor = actor;
        this.movePath = movePath;
    }
}

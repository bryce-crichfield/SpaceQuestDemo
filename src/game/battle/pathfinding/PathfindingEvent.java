package game.battle.pathfinding;

import game.battle.world.Actor;
import game.battle.world.Tile;
import game.event.Event;

import java.util.List;

public class PathfindingEvent extends Event {
    public final Actor actor;
    public final List<Tile> movePath;

    public PathfindingEvent(Actor actor, List<Tile> movePath) {
        this.actor = actor;
        this.movePath = movePath;
    }
}

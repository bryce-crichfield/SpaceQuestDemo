package game.state.battle.event;

import game.event.Event;
import game.state.battle.world.Actor;
import game.state.battle.world.Tile;

import java.util.List;

public class ActorMoved {
    public static final Event<ActorMoved> event = new Event<>();

    public final Actor actor;
    public final List<Tile> movePath;

    public ActorMoved(Actor actor, List<Tile> movePath) {
        this.actor = actor;
        this.movePath = movePath;
    }
}

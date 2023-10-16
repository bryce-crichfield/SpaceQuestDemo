package game.state.battle.event;

import game.event.Event;
import game.state.battle.model.actor.Actor;
import game.state.battle.model.world.Tile;

import java.util.List;

public class ActionActorMoved {
    public static final Event<ActionActorMoved> event = new Event<>();

    public final Actor actor;
    public final List<Tile> movePath;

    public ActionActorMoved(Actor actor, List<Tile> movePath) {
        this.actor = actor;
        this.movePath = movePath;
    }
}

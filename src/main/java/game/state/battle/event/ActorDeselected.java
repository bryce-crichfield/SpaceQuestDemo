package game.state.battle.event;

import game.event.Event;
import game.state.battle.world.Actor;

public class ActorDeselected {
    public static final Event<ActorDeselected> event = new Event<>();

    public final Actor actor;

    public ActorDeselected(Actor actor) {
        this.actor = actor;
    }
}

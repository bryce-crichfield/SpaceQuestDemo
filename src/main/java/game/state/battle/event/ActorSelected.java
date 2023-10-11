package game.state.battle.event;

import game.event.Event;
import game.state.battle.world.Actor;

public class ActorSelected {
    public static final Event<ActorSelected> event = new Event<>();

    public final Actor actor;

    public ActorSelected(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }
}

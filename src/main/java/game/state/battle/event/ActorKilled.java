package game.state.battle.event;

import game.event.Event;
import game.state.battle.model.Actor;

public class ActorKilled {
    public static final Event<Actor> event = new Event<>();
}

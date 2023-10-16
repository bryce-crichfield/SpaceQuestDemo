package game.state.battle.event;

import game.event.Event;
import game.state.battle.model.actor.Actor;

public enum ActorUnselected {
    ;
    public static final Event<Actor> event = new Event<>();
}

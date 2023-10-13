package game.state.battle.event;

import game.event.Event;
import game.state.battle.model.Actor;

public enum ActorSelected {
    ;
    public static final Event<Actor> event = new Event<>();
}

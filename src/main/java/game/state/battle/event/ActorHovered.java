package game.state.battle.event;

import game.event.Event;
import game.state.battle.model.Actor;

public class ActorHovered {
    public static Event<Actor> event = new Event<>();
}
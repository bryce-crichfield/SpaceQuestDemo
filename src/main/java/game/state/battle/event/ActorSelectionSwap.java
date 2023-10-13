package game.state.battle.event;

import game.event.Event;
import game.state.battle.model.Actor;

public class ActorSelectionSwap {
    public static final Event<Actor> event = new Event<>();
}

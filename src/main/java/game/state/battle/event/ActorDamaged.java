package game.state.battle.event;

import game.event.Event;
import game.state.battle.world.Actor;

public class ActorDamaged {
    public static final Event<Actor> event = new Event<>();
}

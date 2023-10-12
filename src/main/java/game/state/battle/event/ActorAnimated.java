package game.state.battle.event;

import game.event.Event;
import game.state.battle.world.Actor;
import game.state.battle.world.ActorAnimation;

public class ActorAnimated {
    public static final Event<Actor> event = new Event<>();
}

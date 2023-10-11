package game.state.battle.event;

import game.event.Event;
import game.state.battle.world.Actor;

public class ActorKilled {
    public static final Event<ActorKilled> event = new Event<>();
    Actor dead;

    public ActorKilled(Actor dead) {
        this.dead = dead;
    }

    public Actor getDead() {
        return dead;
    }
}

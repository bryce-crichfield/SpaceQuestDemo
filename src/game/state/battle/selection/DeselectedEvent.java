package game.state.battle.selection;

import game.state.battle.world.Actor;

public class DeselectedEvent {
    public final Actor actor;

    public DeselectedEvent(Actor actor) {
        this.actor = actor;
    }
}

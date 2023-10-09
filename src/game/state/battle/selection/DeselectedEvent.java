package game.state.battle.selection;

import game.state.battle.world.Actor;

public class DeselectedEvent extends SelectionEvent {
    public final Actor actor;

    public DeselectedEvent(Actor actor) {
        this.actor = actor;
    }
}

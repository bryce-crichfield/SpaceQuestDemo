package game.battle.selection;

import game.battle.world.Actor;

public class DeselectedEvent extends SelectionEvent {
    public final Actor actor;

    public DeselectedEvent(Actor actor) {
        this.actor = actor;
    }
}

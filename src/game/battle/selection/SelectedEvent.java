package game.battle.selection;

import game.battle.world.Actor;

public class SelectedEvent extends SelectionEvent {
    public final Actor actor;

    public SelectedEvent(Actor actor) {
        this.actor = actor;
    }
}

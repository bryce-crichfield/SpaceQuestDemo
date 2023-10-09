package game.state.battle.selection;

import game.state.battle.world.Actor;

public class SelectedEvent extends SelectionEvent {
    public final Actor actor;

    public SelectedEvent(Actor actor) {
        this.actor = actor;
    }
}

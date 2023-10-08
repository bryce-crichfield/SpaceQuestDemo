package game.battle.selection;

import game.battle.Actor;

import java.util.Optional;

public class SelectedEvent extends SelectionEvent {
    public final Actor actor;

    public SelectedEvent(Actor actor) {
        this.actor = actor;
    }
}
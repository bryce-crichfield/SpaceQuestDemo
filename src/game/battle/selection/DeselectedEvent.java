package game.battle.selection;

import game.battle.Actor;

import java.util.Optional;

public class DeselectedEvent extends SelectionEvent {
    public final Actor actor;

    public DeselectedEvent(Actor actor) {
        this.actor = actor;
    }
}

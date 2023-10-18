package game.state.battle.player;

import game.state.battle.event.ActorSelected;
import game.state.battle.event.ActorUnselected;
import game.state.battle.model.Actor;
import game.state.battle.model.World;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class PlayerMode extends Mode {
    protected final Cursor cursor;
    protected Optional<Actor> actor;

    protected PlayerMode(World world, Cursor cursor) {
        super(world);
        this.cursor = cursor;
        this.actor = Optional.empty();
    }

    protected PlayerMode(World world, Cursor cursor, Actor actor) {
        super(world);
        if (actor == null) {
            throw new IllegalArgumentException("Actor cannot be null");
        }
        this.cursor = cursor;
        this.actor = Optional.of(actor);
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Optional<Actor> getActor() {
        return actor;
    }
    public final void selectActor() {
        int cursorX = cursor.getCursorX();
        int cursorY = cursor.getCursorY();

        Optional<Actor> hovered = world.getActorByPosition(cursorX, cursorY);

        if (hovered.isEmpty()) {
            return;
        }

        if (hovered.get().isPlayer()) {
            if (hovered.get().isWaiting())
                return;
            actor = hovered;
            ActorSelected.event.fire(hovered.get());
        }
    }

    public final void deselectActor() {
        if (actor.isEmpty())
            return;

        ActorUnselected.event.fire(actor.get());
        actor = Optional.empty();
    }

    @Override
    public final boolean isDone() {
        List<Actor> playerActorsNotWaiting = world.getActors().stream()
                .filter(Actor::isPlayer)
                .filter(actor -> !actor.isWaiting())
                .toList();

        return playerActorsNotWaiting.isEmpty();
    }
}

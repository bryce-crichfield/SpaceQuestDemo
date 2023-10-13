package game.state.battle.util;

import game.state.battle.event.ActorHovered;
import game.state.battle.event.ActorUnhovered;
import game.state.battle.event.CursorMoved;
import game.state.battle.model.Actor;
import game.state.battle.model.World;

import java.util.Optional;

public class Hoverer {
    private final World world;
    private int cursorX;
    private int cursorY;

    public Hoverer(World world) {
        this.world = world;
    }

    public void onCursorMoved(Cursor cursor) {
        Optional<Actor> currentHoveredActor = world.getActors().stream()
                .filter(actor -> actor.getX() == this.cursorX && actor.getY() == this.cursorY)
                .findFirst();

        this.cursorX = cursor.getCursorX();
        this.cursorY = cursor.getCursorY();

        Optional<Actor> newlyHoveredActor = world.getActors().stream()
                .filter(actor -> actor.getX() == this.cursorX && actor.getY() == this.cursorY)
                .findFirst();

        if (currentHoveredActor.isPresent() && newlyHoveredActor.isEmpty()) {
            ActorUnhovered.event.fire(currentHoveredActor.get());
        }

        newlyHoveredActor.ifPresent(actor -> ActorHovered.event.fire(actor));
    }
}

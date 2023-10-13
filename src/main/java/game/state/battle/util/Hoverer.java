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

    public void onCursorMoved(CursorMoved moved) {
        Optional<Actor> currentHoveredActor = world.getActors().stream()
                .filter(actor -> actor.getX() == this.cursorX && actor.getY() == this.cursorY)
                .findFirst();

        this.cursorX = moved.cursor.getCursorX();
        this.cursorY = moved.cursor.getCursorY();

        Optional<Actor> newlyHoveredActor = world.getActors().stream()
                .filter(actor -> actor.getX() == this.cursorX && actor.getY() == this.cursorY)
                .findFirst();

        if (currentHoveredActor.isPresent() && newlyHoveredActor.isEmpty()) {
            ActorUnhovered.event.fire(currentHoveredActor.get());
        }

        if (newlyHoveredActor.isPresent()) {
            ActorHovered.event.fire(newlyHoveredActor.get());
        }
    }
}

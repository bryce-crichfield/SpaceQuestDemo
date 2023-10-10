package game.state.battle.util;

import game.io.Keyboard;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.CursorMoved;
import game.state.battle.world.Actor;
import game.state.battle.world.World;

import java.util.Optional;

public class Selector {
    private final World world;
    private Optional<Actor> currentlySelectedActor;
    private int cursorX = 0;
    private int cursorY = 0;

    public Selector(World world) {
        this.world = world;
        this.currentlySelectedActor = Optional.empty();

    }

    public void onCursorMoved(CursorMoved event) {
        cursorX = event.cursor.getCursorX();
        cursorY = event.cursor.getCursorY();
    }

    public void onKeyPressed(Integer keyCode) {
        Optional<Actor> hovered = world.getActorByPosition(cursorX, cursorY);

        boolean primaryPressed = keyCode == Keyboard.PRIMARY;
        boolean secondaryPressed = keyCode == Keyboard.SECONDARY;

        // Selection is concerned with 4 conditions
        // 1. Secondary pressed and an actor is selected -> deselect
        boolean secondaryPressedWithActorSelected = secondaryPressed && currentlySelectedActor.isPresent();
        // 2. Primary pressed and no actor is selected, and we clicked on an actor -> select
        boolean primaryPressedWithNoActorSelectedAndHovered = primaryPressed && currentlySelectedActor.isEmpty() && hovered.isPresent();
        // 3. Primary pressed and an actor is selected, and we clicked on the same actor -> deselect
        boolean primaryPressedWithActorSelectedAndHovered = primaryPressed && currentlySelectedActor.isPresent() && hovered.isPresent() && hovered.get() == currentlySelectedActor.get();
        // 4. Primary pressed and an actor is selected, and we clicked on a different actor -> deselect and select
        boolean primaryPressedWithActorSelectedAndHoveredDifferent = primaryPressed && currentlySelectedActor.isPresent() && hovered.isPresent() && hovered.get() != currentlySelectedActor.get();
        // 5. Primary pressed and an actor is selected, and we clicked on an empty tile -> deselect
        boolean primaryPressedWithActorSelectedAndHoveredEmpty = primaryPressed && currentlySelectedActor.isPresent() && hovered.isEmpty();

        if (secondaryPressedWithActorSelected) {
            deselectActor();
        } else if (primaryPressedWithNoActorSelectedAndHovered) {
            selectActor(hovered.get());
        } else if (primaryPressedWithActorSelectedAndHovered) {
            deselectActor();
        } else if (primaryPressedWithActorSelectedAndHoveredDifferent) {
            deselectActor();
            selectActor(hovered.get());
        } else if (primaryPressedWithActorSelectedAndHoveredEmpty) {
            deselectActor();
        }
    }

    private void selectActor(Actor actor) {
        currentlySelectedActor = Optional.of(actor);
        ActorSelected.event.fire(new ActorSelected(currentlySelectedActor.get()));
    }

    public void deselectActor() {
        currentlySelectedActor.ifPresent(actor -> {
            ActorDeselected.event.fire(new ActorDeselected(actor));
        });
        currentlySelectedActor = Optional.empty();
    }
}

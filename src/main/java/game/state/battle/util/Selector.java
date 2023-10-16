package game.state.battle.util;

import game.io.Keyboard;
import game.state.battle.event.ActorUnselected;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.ActorSelectionSwap;
import game.state.battle.model.actor.Actor;
import game.state.battle.model.world.World;

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

    public void onCursorMoved(Cursor cursor) {
        cursorX = cursor.getCursorX();
        cursorY = cursor.getCursorY();
    }

    public void onKeyPressed(Integer keyCode) {
        Optional<Actor> hovered = world.getActorByPosition(cursorX, cursorY);

        boolean primaryPressed = keyCode == Keyboard.PRIMARY;
        boolean secondaryPressed = keyCode == Keyboard.SECONDARY;

        // Selection is concerned with 4 conditions
        // DEPRECATED: This doesn't allow the modal controllers to use secondary to exit.  Controllers must handle this case.
        // 1. Secondary pressed and an actor is selected -> deselect
        boolean secondaryPressedWithActorSelected = secondaryPressed && currentlySelectedActor.isPresent();
        // 2. Primary pressed and no actor is selected, and we clicked on an actor -> select
        boolean primaryPressedWithNoActorSelectedAndHovered = primaryPressed && currentlySelectedActor.isEmpty() && hovered.isPresent();
        // 3. Primary pressed and an actor is selected, and we clicked on the same actor -> deselect
        boolean primaryPressedWithActorSelectedAndHovered = primaryPressed && currentlySelectedActor.isPresent() && hovered.isPresent() && hovered.get() == currentlySelectedActor.get();
        // 4. Primary pressed and an actor is selected, and we clicked on a different actor -> deselect and select
        boolean primaryPressedWithActorSelectedAndHoveredDifferent = primaryPressed && currentlySelectedActor.isPresent() && hovered.isPresent() && hovered.get() != currentlySelectedActor.get();
        // DEPRECATED: This doesn't allow the modal controllers to use primary.  Controllers must handle this case.
        // 5. Primary pressed and an actor is selected, and we clicked on an empty tile -> deselect
        boolean primaryPressedWithActorSelectedAndHoveredEmpty = primaryPressed && currentlySelectedActor.isPresent() && hovered.isEmpty();

        if (secondaryPressedWithActorSelected) {
            // DEPRECATED: See above
        } else if (primaryPressedWithNoActorSelectedAndHovered) {
            selectActor(hovered.get());
        } else if (primaryPressedWithActorSelectedAndHovered) {
            // DEPRECATED: Caused issue in SelectMoveModalController
//            deselectActor();
        } else if (primaryPressedWithActorSelectedAndHoveredDifferent) {
            ActorUnselected.event.fire(currentlySelectedActor.get());
            currentlySelectedActor = hovered;
            ActorSelectionSwap.event.fire(currentlySelectedActor.get());
            ActorSelected.event.fire(currentlySelectedActor.get());
        } else if (primaryPressedWithActorSelectedAndHoveredEmpty) {
            // DEPRECATED: See above
        }
    }

    private void selectActor(Actor actor) {
        currentlySelectedActor = Optional.of(actor);
        ActorSelected.event.fire(currentlySelectedActor.get());
    }

    public void deselectActor() {
        currentlySelectedActor.ifPresent(ActorUnselected.event::fire);
        currentlySelectedActor = Optional.empty();
    }

    public Optional<Actor> getCurrentlySelectedActor() {
        return currentlySelectedActor;
    }
}

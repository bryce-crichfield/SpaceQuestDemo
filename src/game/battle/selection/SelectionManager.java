package game.battle.selection;

import game.Keyboard;
import game.battle.cursor.CursorEvent;
import game.battle.world.Actor;
import game.battle.world.World;
import game.event.EventEmitter;
import game.event.EventListener;
import game.event.EventSource;

import java.util.Optional;

public class SelectionManager implements EventSource<SelectionEvent> {
    private final EventEmitter<SelectionEvent> emitter;
    private final Keyboard keyboard;
    private final World world;
    private Optional<Actor> currentlySelectedActor;
    private int cursorX = 0;
    private int cursorY = 0;
    private final EventListener<CursorEvent> cursorEventListener = event -> {
        cursorX = event.cursorCamera.getCursorX();
        cursorY = event.cursorCamera.getCursorY();
    };
    public SelectionManager(Keyboard keyboard, World world) {
        this.keyboard = keyboard;
        this.world = world;
        this.emitter = new EventEmitter<>();
        this.currentlySelectedActor = Optional.empty();
    }

    public EventListener<CursorEvent> getCursorEventListener() {
        return cursorEventListener;
    }

    public void onUpdate() {
        Optional<Actor> hovered = world.getActorByPosition(cursorX, cursorY);

        boolean primaryPressed = keyboard.pressed(Keyboard.PRIMARY);
        boolean secondaryPressed = keyboard.pressed(Keyboard.SECONDARY);

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
        emitter.fireEvent(new SelectedEvent(currentlySelectedActor.get()));
    }

    private void deselectActor() {
        currentlySelectedActor.ifPresent(actor -> {
            emitter.fireEvent(new DeselectedEvent(actor));
        });
        currentlySelectedActor = Optional.empty();
    }

    public Optional<Actor> getCurrentlySelectedActor() {
        return currentlySelectedActor;
    }

    @Override
    public EventEmitter<SelectionEvent> getEmitter() {
        return emitter;
    }
}

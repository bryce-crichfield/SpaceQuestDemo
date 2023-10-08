package game.battle.selection;

import game.Keyboard;
import game.battle.Actor;
import game.battle.CursorCamera;
import game.battle.World;
import game.event.EventEmitter;
import game.event.EventListener;
import game.event.EventSource;

import java.util.Optional;

public class SelectionManager implements EventSource<SelectionEvent>, EventListener<CursorCamera.CursorMovedEvent> {
    private final EventEmitter<SelectionEvent> emitter;
    private final Keyboard keyboard;
    private final World world;
    private Optional<Actor> currentlySelectedActor;

    private int cursorX = 0;
    private int cursorY = 0;

    public SelectionManager(Keyboard keyboard, World world) {
        this.keyboard = keyboard;
        this.world = world;
        this.emitter = new EventEmitter<>();
        this.currentlySelectedActor = Optional.empty();
    }

    public void onUpdate() {

        Optional<Actor> hovered = world.getActorByPosition(cursorX, cursorY);

        boolean primaryPressed = keyboard.pressed(Keyboard.PRIMARY);
        boolean secondaryPressed = keyboard.pressed(Keyboard.SECONDARY);



        // Handle deselection by secondary click
        if (secondaryPressed && currentlySelectedActor.isPresent()) {
            emitter.fireEvent(new DeselectedEvent(currentlySelectedActor.get()));
            currentlySelectedActor = Optional.empty();
        }

        if (!primaryPressed) {
            return;
        }

        // Handle selection by primary click (3 cases)
        // 1. There is no actor selected, and we clicked on an actor
        boolean clickedOnActorWithNoActorSelected = hovered.isPresent() && currentlySelectedActor.isEmpty();
        if (clickedOnActorWithNoActorSelected) {
            selectActor(hovered.get());
            return;
        }
        // 2. There is an actor selected, and we clicked on the same actor
        boolean clickedOnSameActor = currentlySelectedActor.isPresent() && hovered.isPresent() && hovered.get() == currentlySelectedActor.get();
        if (clickedOnSameActor) {
            deselectActor();
            return;
        }

        // 3. There is an actor selected, and we clicked on a different actor
        boolean clickedOnDifferentActorWithActorSelected = hovered.isPresent();
        if (clickedOnDifferentActorWithActorSelected) {
            deselectActor();
            selectActor(hovered.get());
            return;
        }

        return;
    }

    @Override
    public EventEmitter<SelectionEvent> getEmitter() {
        return emitter;
    }

    @Override
    public void onEvent(CursorCamera.CursorMovedEvent event) {
        cursorX = event.cursorCamera.getCursorX();
        cursorY = event.cursorCamera.getCursorY();
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
}

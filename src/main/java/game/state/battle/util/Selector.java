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

        boolean primaryPressedWithNoActorSelectedAndHovered = primaryPressed && currentlySelectedActor.isEmpty() && hovered.isPresent();
        if (primaryPressedWithNoActorSelectedAndHovered) {
            if (hovered.get().isPlayer())
                selectActor(hovered.get());
        }
    }

    private void selectActor(Actor actor) {
        if (actor.isWaiting()) return;
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

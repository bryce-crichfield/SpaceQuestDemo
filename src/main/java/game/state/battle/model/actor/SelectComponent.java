package game.state.battle.model.actor;

import game.io.Keyboard;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.ActorUnselected;
import game.state.battle.util.Cursor;

public class SelectComponent {
    private final Actor self;
    private boolean isSelected = false;
    private boolean isPrimaryPressed = false;
    public SelectComponent(Actor actor) {
        self = actor;
    }

    public void onKeyPressed(int key) {
        if (key == Keyboard.PRIMARY) {
            isPrimaryPressed = true;
        }
    }

    public void onKeyReleased(int key) {
        if (key == Keyboard.PRIMARY) {
            isPrimaryPressed = false;
        }
    }

    public void onCursorMoved(Cursor cursor) {
        boolean cursorHovers = cursor.getCursorX() == self.getX() && cursor.getCursorY() == self.getY();

        if (!isSelected && cursorHovers && isPrimaryPressed) {
            onSelected();
        } else if (isSelected && !cursorHovers && isPrimaryPressed) {
            onUnselected();
        }
    }

    private void onSelected() {
        isSelected = true;
        ActorSelected.event.fire(self);
    }

    private void onUnselected() {
        isSelected = false;
        ActorUnselected.event.fire(self);
    }
}

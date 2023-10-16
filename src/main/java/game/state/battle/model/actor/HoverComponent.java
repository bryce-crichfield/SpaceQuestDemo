package game.state.battle.model.actor;

import game.state.battle.event.ActorHovered;
import game.state.battle.event.ActorUnhovered;
import game.state.battle.util.Cursor;

public class HoverComponent {
    private final Actor self;
    private boolean isHovered = false;

    public HoverComponent(Actor actor) {
        self = actor;
    }

    public void onCursorMoved(Cursor cursor) {
        boolean cursorHovers = cursor.getCursorX() == self.getX() && cursor.getCursorY() == self.getY();

        if (isHovered && !cursorHovers) {
            onUnhovered();
        } else if (!isHovered && cursorHovers) {
            onHovered();
        }
    }

    private void onHovered() {
        isHovered = true;
        ActorHovered.event.fire(self);
    }

    private void onUnhovered() {
        isHovered = false;
        ActorUnhovered.event.fire(self);
    }
}

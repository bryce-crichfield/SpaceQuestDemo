package game.battle.cursor;

import game.event.Event;

public class CursorEvent extends Event {
    public final CursorCamera cursorCamera;

    public CursorEvent(CursorCamera cursorCamera) {
        this.cursorCamera = cursorCamera;
    }
}

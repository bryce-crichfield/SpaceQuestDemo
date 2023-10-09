package game.state.battle.event;

import game.event.Event;
import game.state.battle.cursor.CursorCamera;

public class CursorMoved {
    public static final Event<CursorMoved> event = new Event<>();

    public final CursorCamera cursorCamera;

    public CursorMoved(CursorCamera cursorCamera) {
        this.cursorCamera = cursorCamera;
    }
}

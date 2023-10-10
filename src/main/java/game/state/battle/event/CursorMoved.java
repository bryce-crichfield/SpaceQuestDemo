package game.state.battle.event;

import game.event.Event;
import game.state.battle.util.Cursor;

public class CursorMoved {
    public static final Event<CursorMoved> event = new Event<>();

    public final Cursor cursor;

    public CursorMoved(Cursor cursor) {
        this.cursor = cursor;
    }
}

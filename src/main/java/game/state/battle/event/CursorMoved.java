package game.state.battle.event;

import game.event.Event;
import game.state.battle.util.Cursor;

public class CursorMoved {
    public static final Event<Cursor> event = new Event<>();
}

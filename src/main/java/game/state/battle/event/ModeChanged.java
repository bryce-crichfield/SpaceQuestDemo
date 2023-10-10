package game.state.battle.event;

import game.event.LazyEvent;
import game.state.battle.mode.ActionMode;

public class ModeChanged {
    public static LazyEvent<ActionMode> event = new LazyEvent<>();
}

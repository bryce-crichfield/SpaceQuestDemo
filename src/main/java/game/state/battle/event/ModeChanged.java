package game.state.battle.event;

import game.event.LazyEvent;
import game.state.battle.BattleState;
import game.state.battle.controller.InteractionMode;

import java.util.function.Consumer;
import java.util.function.Function;

public class ModeChanged {
    public static LazyEvent<Function<BattleState, InteractionMode>> event = new LazyEvent<>();
}

package game.state.battle.event;

import game.event.LazyEvent;
import game.state.battle.BattleState;
import game.state.battle.controller.PlayerController;

import java.util.function.Function;

public enum ControllerTransition {
    ;
    public static LazyEvent<Function<BattleState, PlayerController>> defer = new LazyEvent<>();
}

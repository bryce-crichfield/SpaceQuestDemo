package game.state.battle.event;

import game.event.LazyEvent;
import game.state.battle.BattleState;
import game.state.battle.controller.ModalController;

import java.util.function.Function;

public class ControllerTransition {
    public static LazyEvent<Function<BattleState, ModalController>> defer = new LazyEvent<>();
}

package game.state.battle.event;

import game.event.LazyEvent;
import game.state.battle.BattleState;
import game.state.battle.player.PlayerMode;

import java.util.function.Function;
import java.util.function.Supplier;

public enum ControllerTransition {
    ;
    public static LazyEvent<Supplier<PlayerMode>> defer = new LazyEvent<>();
}

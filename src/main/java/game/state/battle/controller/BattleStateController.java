package game.state.battle.controller;

import game.event.SubscriptionManager;
import game.state.battle.BattleState;

import java.time.Duration;

public abstract class BattleStateController extends SubscriptionManager {
    private final BattleState battleState;

    protected BattleStateController(BattleState battleState) {
        this.battleState = battleState;
    }

    public BattleState getBattleState() {
        return battleState;
    }

    public abstract void onEnter();

    public void onExit() {
        unsubscribeAll();
    }
}

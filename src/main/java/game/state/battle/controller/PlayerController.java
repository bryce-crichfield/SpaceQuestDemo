package game.state.battle.controller;

import game.event.SubscriptionManager;
import game.state.battle.BattleState;

public abstract class PlayerController extends SubscriptionManager {
    private final BattleState battleState;

    protected PlayerController(BattleState battleState) {
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

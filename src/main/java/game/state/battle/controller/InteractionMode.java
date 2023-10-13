package game.state.battle.controller;

import game.event.SubscriptionManager;
import game.state.battle.BattleState;

import java.time.Duration;

public abstract class InteractionMode extends SubscriptionManager {
    private final BattleState battleState;

    protected InteractionMode(BattleState battleState) {
        this.battleState = battleState;
    }

    public BattleState getBattleState() {
        return battleState;
    }

    public abstract void onEnter();

    public abstract void onUpdate(Duration delta);

    public void onExit() {
        unsubscribeAll();
    }
}

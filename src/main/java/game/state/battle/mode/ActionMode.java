package game.state.battle.mode;

import game.event.SubscriptionManager;
import game.state.battle.BattleState;

import java.time.Duration;

public abstract class ActionMode extends SubscriptionManager {
    private final BattleState battleState;

    protected ActionMode(BattleState battleState) {
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

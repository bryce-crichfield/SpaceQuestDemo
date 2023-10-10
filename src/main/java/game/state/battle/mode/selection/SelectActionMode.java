package game.state.battle.mode.selection;

import game.Game;
import game.event.SubscriptionManager;
import game.io.Keyboard;
import game.state.battle.event.ActorDeselected;
import game.state.battle.mode.ActionMode;
import game.state.battle.BattleState;
import game.state.battle.mode.ObserverMode;
import game.state.battle.event.ModeChanged;
import game.state.battle.world.Actor;

import java.time.Duration;

public class SelectActionMode extends ActionMode {
    private final SubscriptionManager subscriptions = new SubscriptionManager();
    private final BattleState battleState;
    private final ActionSelectMenu actionSelectMenu;
    private final Actor actor;

    public SelectActionMode(BattleState battleState, Actor actor) {
        super(battleState);
        this.battleState = battleState;
        this.actor = actor;
        Game game = battleState.getGame();
        int menuWidth = 3 * game.TILE_SIZE;
        int menuHeight = 3 * game.TILE_SIZE;
        int menuX = game.SCREEN_WIDTH - menuWidth - game.TILE_SIZE;
        int menuY = game.SCREEN_HEIGHT - menuHeight - game.TILE_SIZE;
        actionSelectMenu = new ActionSelectMenu(game, menuX, menuY, menuWidth, menuHeight, battleState, actor);
    }

    @Override
    public void onEnter() {
        on(BattleState.onGuiRender).run(actionSelectMenu::onRender);
        on(Keyboard.keyPressed).run( keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ActorDeselected.event.fire(new ActorDeselected(actor));
                ModeChanged.event.fire(new ObserverMode(battleState));
            }
        });
    }

    @Override
    public void onUpdate(Duration delta) {
        actionSelectMenu.onUpdate(delta);
    }
}

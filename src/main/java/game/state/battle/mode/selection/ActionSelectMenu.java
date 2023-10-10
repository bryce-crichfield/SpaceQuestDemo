package game.state.battle.mode.selection;

import game.Game;
import game.state.battle.BattleState;
import game.state.battle.event.ModeChanged;
import game.state.battle.mode.move.MoveActionMode;
import game.state.battle.mode.attack.AttackActionMode;
import game.state.battle.world.Actor;
import game.widget.ButtonWidget;
import game.widget.Menu;

public class ActionSelectMenu extends Menu {

    private final ButtonWidget attackButton;
    private final ButtonWidget moveButton;

    public ActionSelectMenu(Game game, int x, int y, int width, int height, BattleState state, Actor actor) {
        super(game, x, y, width, height);

        attackButton = new ButtonWidget("Attack", game, () -> {
            ModeChanged.event.fire(new AttackActionMode(state));
        });

        moveButton = new ButtonWidget("Move", game, () -> {
            MoveActionMode moveActionMode = new MoveActionMode(state, actor);
            ModeChanged.event.fire(moveActionMode);
        });

        this.setWidgets(attackButton, moveButton);
    }
}

package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ControllerTransition;

import java.awt.*;
import java.time.Duration;

public class ObserverController extends BattleStateController {
    public ObserverController(BattleState battleState) {
        super(battleState);
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.WHITE);

        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getSelector()::onKeyPressed);

        on(CursorMoved.event).run(getBattleState().getHoverer()::onCursorMoved);
        on(CursorMoved.event).run(getBattleState().getSelector()::onCursorMoved);

        on(getBattleState().getOnWorldRender()).run(getBattleState().getCursor()::onRender);

        on(ActorSelected.event).run(event -> ControllerTransition.defer.fire(SelectActionController::new));
    }

    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);
    }
}

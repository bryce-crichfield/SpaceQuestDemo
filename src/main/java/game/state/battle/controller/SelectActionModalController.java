package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.Hud;
import game.state.battle.model.Actor;

import java.util.Optional;

public class SelectActionModalController extends ModalController {
    protected SelectActionModalController(BattleState battleState) {
        super(battleState);
    }

    @Override
    public void onEnter() {
        Hud hud = getBattleState().getHud();

        on(Keyboard.keyPressed).run(hud.getActions()::onKeyPressed);
        on(Keyboard.keyPressed).run(keyCode -> {
            boolean isSecondary = keyCode == Keyboard.SECONDARY;
            if (!isSecondary) return;
            Optional<Actor> actor = getBattleState().getSelector().getCurrentlySelectedActor();
            if (actor.isEmpty()) {
                throw new IllegalStateException("No actor selected in the select action mode");
            }
            ActorDeselected.event.fire(actor.get());
            ControllerTransition.defer.fire(ObserverModalController::new);
        });

        hud.getActions().setVisible(true);
    }

    @Override
    public void onExit() {
        super.onExit();
        getBattleState().getHud().getActions().setVisible(false);
    }
}

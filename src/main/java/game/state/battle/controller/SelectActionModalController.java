package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorHovered;
import game.state.battle.event.ActorUnselected;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.Hud;
import game.state.battle.model.actor.Actor;

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
            if (keyCode == Keyboard.SECONDARY) {
                Optional<Actor> actor = getBattleState().getSelector().getCurrentlySelectedActor();
                if (actor.isEmpty()) {
                    throw new IllegalStateException("No actor selected in the select action mode");
                }

                getBattleState().getSelector().deselectActor();
                ControllerTransition.defer.fire(state -> {
                    int cursorX = getBattleState().getCursor().getCursorX();
                    int cursorY = getBattleState().getCursor().getCursorY();
                    if (cursorX == actor.get().getX() && cursorY == actor.get().getY()) {
                        ActorHovered.event.fire(actor.get());
                    }
                    return new ObserverModalController(state);
                });
            }
        });

        hud.getActions().setVisible(true);
    }

    @Override
    public void onExit() {
        super.onExit();
        getBattleState().getHud().getActions().setVisible(false);
    }
}

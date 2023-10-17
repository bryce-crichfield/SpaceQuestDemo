package game.state.battle.controller;

import game.event.Event;
import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorHovered;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.HudActions;
import game.state.battle.hud.HudStats;
import game.state.battle.model.actor.Actor;

import java.util.Optional;

public class SelectActionPlayerController extends PlayerController {
    private final Actor selected;
    private final HudStats hudStats;
    private final HudActions hudActions;

    protected SelectActionPlayerController(BattleState battleState, Actor selected) {
        super(battleState);

        this.selected = selected;

        Event<Actor> onChange = new Event<>();
        hudStats = new HudStats(5, 65, onChange);
        hudStats.setVisible(true);
        onChange.fire(selected);

        hudActions = new HudActions(40, 65, 30, 30, onChange);
        hudActions.setVisible(true);
    }

    private void onKeyPressed(int keycode) {
        if (keycode == Keyboard.SECONDARY) {
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
                return new ObserverPlayerController(state);
            });
        }
    }

    @Override
    public void onEnter() {
        on(Keyboard.keyPressed).run(hudActions::onKeyPressed);
        on(Keyboard.keyPressed).run(this::onKeyPressed);
        on(getBattleState().getOnGuiRender()).run(hudStats::onRender);
        on(getBattleState().getOnGuiRender()).run(hudActions::onRender);
    }

    @Override
    public void onExit() {
        super.onExit();
    }
}

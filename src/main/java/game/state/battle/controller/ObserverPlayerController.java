package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.HudStats;
import game.state.battle.model.actor.Actor;
import game.event.Event;
import game.state.battle.util.Cursor;

import java.awt.*;
import java.util.Optional;

public class ObserverPlayerController extends PlayerController {
    private final Event<Actor> onChangeHovered;
    private final HudStats hoveredActorStats;

    public ObserverPlayerController(BattleState battleState) {
        super(battleState);
        onChangeHovered = new Event<>();
        hoveredActorStats = new HudStats(5, 5, onChangeHovered);
        hoveredActorStats.setVisible(false);
    }

    private void forceHoveredActorStats() {
        int cx = getBattleState().getCursor().getCursorX();
        int cy = getBattleState().getCursor().getCursorY();
        Optional<Actor> hov = getBattleState().getWorld().getActorByPosition(cx, cy);
        hov.ifPresent(actor -> {
            onChangeHovered.fire(actor);
            hoveredActorStats.setVisible(true);
        });
    }

    @Override
    public void onEnter() {
        forceHoveredActorStats();

        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.WHITE);

        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getSelector()::onKeyPressed);

        on(getBattleState().getOnWorldRender()).run(getBattleState().getCursor()::onRender);

        on(ActorSelected.event).run(actor -> {
            ControllerTransition.defer.fire(state -> new SelectActionPlayerController(state, actor));
        });

        on(CursorMoved.event).run(this::onCursorMoved);
        on(getBattleState().getOnGuiRender()).run(hoveredActorStats::onRender);

//        getBattleState().getCursor().setPosition(3, 0);
//        Keyboard.keyPressed.fire(Keyboard.PRIMARY);
    }

    private void onCursorMoved(Cursor cursor) {
        int cursorX = cursor.getCursorX();
        int cursorY = cursor.getCursorY();

        Optional<Actor> actor = getBattleState().getWorld().getActorByPosition(cursorX, cursorY);
        if (actor.isEmpty()) {
            hoveredActorStats.setVisible(false);
        }

        if (actor.isPresent()) {

            Actor hovered = actor.get();
            hoveredActorStats.setVisible(true);
            onChangeHovered.fire(hovered);
        }
    }

    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);
    }
}

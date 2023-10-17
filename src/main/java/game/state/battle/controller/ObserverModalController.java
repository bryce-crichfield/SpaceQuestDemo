package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.HudStats;
import game.state.battle.model.actor.Actor;
import game.event.Event;

import java.awt.*;
import java.util.Optional;

public class ObserverModalController extends ModalController {
    private final Event<Actor> onChangeHovered;
    private final HudStats hoveredActorStats;
    public ObserverModalController(BattleState battleState) {
        super(battleState);
        onChangeHovered = new Event<>();
        hoveredActorStats = new HudStats(55, 5, 30, 25, onChangeHovered);
        hoveredActorStats.setVisible(false);
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.WHITE);

        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getSelector()::onKeyPressed);


        on(getBattleState().getOnWorldRender()).run(getBattleState().getCursor()::onRender);

        on(ActorSelected.event).run(actor -> {
            ControllerTransition.defer.fire(state -> new SelectActionModalController(state, actor));
        });

        on(CursorMoved.event).run(cursor -> {
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
        });

        on(getBattleState().getOnGuiRender()).run(hoveredActorStats::onRender);
    }

    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);
    }
}

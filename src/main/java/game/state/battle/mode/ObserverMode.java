package game.state.battle.mode;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ModeChanged;
import game.state.battle.mode.selection.SelectActionMode;
import game.state.battle.util.Selector;
import game.state.battle.world.Actor;

import java.awt.*;
import java.time.Duration;

public class ObserverMode extends ActionMode {

    private final BattleState battleState;
    private final Selector selector;

    public ObserverMode(BattleState battleState) {
        super(battleState);
        this.battleState = battleState;
        this.selector = new Selector(battleState.getWorld());
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.WHITE);

        on(getBattleState().getOnWorldRender()).run(this::onRender);
        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(CursorMoved.event).run(selector::onCursorMoved);
        on(Keyboard.keyPressed).run(selector::onKeyPressed);

        for (Actor actor : battleState.getWorld().getActors()) {
            on(ActorSelected.event).run(actor::onActorSelected);
            on(ActorDeselected.event).run(actor::onActorDeselected);
        }

        on(ActorSelected.event).run((ActorSelected actorSelected) -> {
            ModeChanged.event.fire(new SelectActionMode(battleState, actorSelected.actor));
        });
    }

    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);
    }

    @Override
    public void onUpdate(Duration delta) {
        getBattleState().getCursor().onUpdate(delta);
    }
}

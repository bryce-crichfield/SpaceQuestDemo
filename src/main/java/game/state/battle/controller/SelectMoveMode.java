package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ActorMoved;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ModeChanged;
import game.state.battle.util.Pathfinder;
import game.state.battle.util.Selector;
import game.state.battle.model.Actor;
import game.state.battle.model.World;

import java.awt.*;
import java.time.Duration;
import java.util.Optional;

public class SelectMoveMode extends InteractionMode {
    private final World world;
    private final Pathfinder pathfinder;

    public SelectMoveMode(BattleState battleState) {
        super(battleState);
        this.world = battleState.getWorld();

        Optional<Actor> actor = battleState.getSelector().getCurrentlySelectedActor();
        if (actor.isEmpty())
            throw new IllegalStateException("No actor selected");
        this.pathfinder = new Pathfinder(battleState, world, actor.get());
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.ORANGE);

        on(ActorMoved.event).run(event -> ModeChanged.event.fire(SelectActionMode::new));

        on(getBattleState().getOnWorldRender()).run(this::onWorldRender);

        on(CursorMoved.event).run(pathfinder::onCursorMoved);
        on(CursorMoved.event).run(getBattleState().getSelector()::onCursorMoved);

        on(Keyboard.keyPressed).run(pathfinder::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getSelector()::onKeyPressed);

        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ModeChanged.event.fire(SelectActionMode::new);
            }
        });
    }

    public void onWorldRender(Graphics2D graphics) {
        pathfinder.onRender(graphics);
        getBattleState().getCursor().onRender(graphics);
    }

    @Override
    public void onUpdate(Duration delta) {
        getBattleState().getCursor().onUpdate(delta);
    }
}

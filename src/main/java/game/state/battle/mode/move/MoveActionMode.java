package game.state.battle.mode.move;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ActorMoved;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ModeChanged;
import game.state.battle.mode.ActionMode;
import game.state.battle.mode.ObserverMode;
import game.state.battle.mode.selection.SelectActionMode;
import game.state.battle.util.Selector;
import game.state.battle.world.Actor;
import game.state.battle.world.World;

import java.awt.*;
import java.time.Duration;

public class MoveActionMode extends ActionMode {
    private final Actor actor;
    private final World world;
    private final Selector selector;
    private final Pathfinder pathfinder;

    public MoveActionMode(BattleState battleState, Actor actor) {
        super(battleState);
        this.actor = actor;
        this.world = battleState.getWorld();
        this.selector = new Selector(world);
        this.pathfinder = new Pathfinder(battleState, world, actor);
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.ORANGE);

        on(ActorMoved.event).run(event -> {
//            ActorDeselected.event.fire(new ActorDeselected(actor));
//            ModeChanged.event.fire(new ObserverMode(getBattleState()));
            ModeChanged.event.fire(new SelectActionMode(getBattleState(), actor));
        });

        on(getBattleState().getOnWorldRender()).run(this::onWorldRender);

        on(CursorMoved.event).run(pathfinder::onCursorMoved);
        on(CursorMoved.event).run(selector::onCursorMoved);

        on(Keyboard.keyPressed).run(pathfinder::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(selector::onKeyPressed);

        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ModeChanged.event.fire(new SelectActionMode(getBattleState(), actor));
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

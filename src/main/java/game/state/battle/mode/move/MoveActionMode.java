package game.state.battle.mode.move;

import game.io.Keyboard;
import game.state.battle.mode.ActionMode;
import game.state.battle.BattleState;
import game.state.battle.mode.ObserverMode;
import game.state.battle.util.Cursor;
import game.state.battle.event.*;
import game.state.battle.mode.selection.SelectActionMode;
import game.state.battle.util.Selector;
import game.state.battle.world.Actor;
import game.state.battle.world.World;

import java.awt.*;
import java.time.Duration;

public class MoveActionMode extends ActionMode {
    private final Actor actor;
    private final World world;
    private final Cursor cursor;
    private final Selector selector;
    private final Pathfinder pathfinder;

    public void onWorldRender(Graphics2D graphics) {
        cursor.onRender(graphics);
        pathfinder.onRender(graphics);
    }

    public MoveActionMode(BattleState battleState, Actor actor) {
        super(battleState);
        this.actor = actor;
        this.world = battleState.getWorld();
        this.cursor = new Cursor(battleState.getCamera(), battleState.getGame(), world);
        this.selector = new Selector(world);
        this.pathfinder = new Pathfinder(world, actor);
    }

    @Override
    public void onEnter() {
        cursor.enterBlinkingMode();
        cursor.setColor(Color.ORANGE);

        on(ActorMoved.event).run(event -> {
            ActorDeselected.event.fire(new ActorDeselected(actor));
            ModeChanged.event.fire(new ObserverMode(getBattleState()));
        });

        on(BattleState.onWorldRender).run(this::onWorldRender);

        on(CursorMoved.event).run(pathfinder::onCursorMoved);
        on(CursorMoved.event).run(selector::onCursorMoved);

        on(Keyboard.keyPressed).run(pathfinder::onKeyPressed);
        on(Keyboard.keyPressed).run(cursor::onKeyPressed);
        on(Keyboard.keyPressed).run(selector::onKeyPressed);

        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ActorDeselected.event.fire(new ActorDeselected(actor));
                ModeChanged.event.fire(new SelectActionMode(getBattleState(), actor));
            }
        });
    }

    @Override
    public void onUpdate(Duration delta) {
        cursor.onUpdate(delta);
    }
}

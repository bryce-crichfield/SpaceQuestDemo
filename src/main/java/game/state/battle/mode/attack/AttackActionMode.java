package game.state.battle.mode.attack;

import game.event.SubscriptionManager;
import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorAttacked;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ModeChanged;
import game.state.battle.mode.ActionMode;
import game.state.battle.mode.ObserverMode;
import game.state.battle.mode.selection.SelectActionMode;
import game.state.battle.util.Selector;
import game.state.battle.world.Actor;
import game.state.battle.world.Raycast;
import game.state.battle.world.Tile;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class AttackActionMode extends ActionMode {
    private final SubscriptionManager subscriptions = new SubscriptionManager();
    private final Selector selector;
    Raycast raycast;
    Actor actor;

    public AttackActionMode(BattleState battleState, Actor selectedActor) {
        super(battleState);
        this.actor = selectedActor;
        this.selector = new Selector(battleState.getWorld());
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.RED);

        on(getBattleState().getOnWorldRender()).run(this::onRender);
        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(CursorMoved.event).run(this::onCursorMoved);

        on(Keyboard.keyPressed).run(this::onKeyPressed);

        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ModeChanged.event.fire(new SelectActionMode(getBattleState(), actor));
            }
        });
    }

    public void onKeyPressed(Integer keyCode) {
        if (keyCode == Keyboard.PRIMARY) {
            getBattleState().getGame().getAudio().play("select.wav");
            ActorAttacked.event.fire(new ActorAttacked(actor, raycast.getTiles()));
            ActorDeselected.event.fire(new ActorDeselected(actor));
            ModeChanged.event.fire(new ObserverMode(getBattleState()));
        }
    }

    //    @Override
    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);

        if (raycast == null) {
            return;
        }
        List<Tile> possiblePath = raycast.getTiles();

        Tile.drawOutline(possiblePath, graphics, Color.RED);

        List<Tile> inRange = getBattleState().getWorld().getTilesInRange((int) actor.getX(), (int) actor.getY(),
                                                                         actor.getAttackDistance()
        );

        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for (Tile tile : inRange) {
            graphics.setColor(Color.RED.darker().darker());
            graphics.fillRect(tile.getX() * 32, tile.getY() * 32, 32, 32);
        }
        graphics.setComposite(originalComposite);

        Tile.drawOutline(inRange, graphics, Color.RED);

    }

    public void onCursorMoved(CursorMoved event) {
        int cursorX = event.cursor.getCursorX();
        int cursorY = event.cursor.getCursorY();
        raycast = getBattleState().getWorld().raycast((int) actor.getX(), (int) actor.getY(), cursorX, cursorY);
    }

    @Override
    public void onUpdate(Duration delta) {
        getBattleState().getCursor().onUpdate(delta);
    }
}

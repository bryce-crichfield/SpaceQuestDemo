package game.state.battle.mode.attack;

import game.event.SubscriptionManager;
import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ModeChanged;
import game.state.battle.mode.ActionMode;
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

        on(BattleState.onWorldRender).run(this::onRender);
        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(CursorMoved.event).run(this::onCursorMoved);

        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ModeChanged.event.fire(new SelectActionMode(getBattleState(), actor));
            }
        });
    }

    //    @Override
    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);

        if (raycast == null) {
            return;
        }
        // Draw the raycast
        List<Tile> tiles = raycast.getTiles();
        for (Tile tile : tiles) {
            List<Tile> neighbors = tile.getNeighbors(tiles);
            boolean hasAbove = neighbors.stream().anyMatch(neighbor -> neighbor.getY() < tile.getY());
            boolean hasBelow = neighbors.stream().anyMatch(neighbor -> neighbor.getY() > tile.getY());
            boolean hasLeft = neighbors.stream().anyMatch(neighbor -> neighbor.getX() < tile.getX());
            boolean hasRight = neighbors.stream().anyMatch(neighbor -> neighbor.getX() > tile.getX());

            System.out.println("hasAbove = " + hasAbove);
            System.out.println("hasBelow = " + hasBelow);
            System.out.println("hasLeft = " + hasLeft);
            System.out.println("hasRight = " + hasRight);
            System.out.println();

            Stroke oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(2));
            graphics.setColor(Color.RED);

            int tileX = tile.getX() * getBattleState().getGame().TILE_SIZE;
            int tileY = tile.getY() * getBattleState().getGame().TILE_SIZE;
            int tileWidth = getBattleState().getGame().TILE_SIZE;
            int tileHeight = getBattleState().getGame().TILE_SIZE;

            if (!hasAbove) {
                graphics.drawLine(tileX, tileY, tileX + tileWidth, tileY);
            }

            if (!hasBelow) {
                graphics.drawLine(tileX, tileY + tileHeight, tileX + tileWidth, tileY + tileHeight);
            }

            if (!hasLeft) {
                graphics.drawLine(tileX, tileY, tileX, tileY + tileHeight);
            }

            if (!hasRight) {
                graphics.drawLine(tileX + tileWidth, tileY, tileX + tileWidth, tileY + tileHeight);
            }

            graphics.setStroke(oldStroke);
        }
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

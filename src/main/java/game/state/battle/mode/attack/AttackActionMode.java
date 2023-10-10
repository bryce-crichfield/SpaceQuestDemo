package game.state.battle.mode.attack;

import game.event.SubscriptionManager;
import game.io.Keyboard;
import game.state.battle.mode.ActionMode;
import game.state.battle.BattleState;
import game.state.battle.util.Selector;
import game.state.battle.world.Raycast;
import game.state.battle.util.Cursor;

import java.awt.*;
import java.time.Duration;

public class AttackActionMode extends ActionMode {
    private final SubscriptionManager subscriptions = new SubscriptionManager();
    private final Cursor cursor;
    private final Selector selector;
    Raycast raycast;

    public AttackActionMode(BattleState battleState) {
        super(battleState);
        this.cursor = new Cursor(battleState.getCamera(), battleState.getGame(), battleState.getWorld());
        this.selector = new Selector(battleState.getWorld());
    }


    @Override
    public void onEnter() {
        cursor.enterBlinkingMode();
        cursor.setColor(Color.RED);

        on(BattleState.onWorldRender).run(this::onRender);
        on(Keyboard.keyPressed).run(cursor::onKeyPressed);
    }

    @Override
    public void onUpdate(Duration delta) {
        cursor.onUpdate(delta);
//
//        if (battleState.selectionManager.getCurrentlySelectedActor().isPresent()) {
//            Actor actor = battleState.selectionManager.getCurrentlySelectedActor().get();
//            raycast = battleState.world.raycast(battleState.cursorCamera.getCursorX(),
//                                                battleState.cursorCamera.getCursorY(), (int) actor.getX(),
//                                                (int) actor.getY()
//            );
//        }
    }

//    @Override
    public void onRender(Graphics2D graphics) {
        cursor.onRender(graphics);
//        battleState.drawWithCamera(graphics, camera -> {
//            battleState.cursorCamera.onRender(camera);
//
//            if (raycast == null) {
//                return;
//            }
//            // Draw the raycast
//            java.util.List<Tile> tiles = raycast.getTiles();
//            for (Tile tile : tiles) {
//                List<Tile> neighbors = tile.getNeighbors(tiles);
//                boolean hasAbove = neighbors.stream().anyMatch(neighbor -> neighbor.getY() < tile.getY());
//                boolean hasBelow = neighbors.stream().anyMatch(neighbor -> neighbor.getY() > tile.getY());
//                boolean hasLeft = neighbors.stream().anyMatch(neighbor -> neighbor.getX() < tile.getX());
//                boolean hasRight = neighbors.stream().anyMatch(neighbor -> neighbor.getX() > tile.getX());
//
//                System.out.println("hasAbove = " + hasAbove);
//                System.out.println("hasBelow = " + hasBelow);
//                System.out.println("hasLeft = " + hasLeft);
//                System.out.println("hasRight = " + hasRight);
//                System.out.println();
//
//                Stroke oldStroke = graphics.getStroke();
//                graphics.setStroke(new BasicStroke(2));
//                graphics.setColor(Color.RED);
//
//                int tileX = tile.getX() * battleState.getGame().TILE_SIZE;
//                int tileY = tile.getY() * battleState.getGame().TILE_SIZE;
//                int tileWidth = battleState.getGame().TILE_SIZE;
//                int tileHeight = battleState.getGame().TILE_SIZE;
//
//                if (!hasAbove) {
//                    graphics.drawLine(tileX, tileY, tileX + tileWidth, tileY);
//                }
//
//                if (!hasBelow) {
//                    graphics.drawLine(tileX, tileY + tileHeight, tileX + tileWidth, tileY + tileHeight);
//                }
//
//                if (!hasLeft) {
//                    graphics.drawLine(tileX, tileY, tileX, tileY + tileHeight);
//                }
//
//                if (!hasRight) {
//                    graphics.drawLine(tileX + tileWidth, tileY, tileX + tileWidth, tileY + tileHeight);
//                }
//
//                graphics.setStroke(oldStroke);
//            }
//        });
    }

    @Override
    public void onExit() {
        subscriptions.unsubscribeAll();
    }
}

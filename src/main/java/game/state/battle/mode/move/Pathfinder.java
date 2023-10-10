package game.state.battle.mode.move;

import game.io.Keyboard;
import game.state.battle.event.ActorMoved;
import game.state.battle.event.CursorMoved;
import game.state.battle.world.Actor;
import game.state.battle.world.Tile;
import game.state.battle.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pathfinder {

    private final World world;
    private final Actor actor;
    int cursorX = 0;
    int cursorY = 0;

    private List<Tile> possiblePath;

    public Pathfinder(World world, Actor actor) {
        this.world = world;
        this.actor = actor;
        possiblePath = new ArrayList<>();
    }

    public void onCursorMoved(CursorMoved event) {
        cursorX = event.cursor.getCursorX();
        cursorY = event.cursor.getCursorY();

        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();
        if (!hoveringOnEmptyTile) {
            possiblePath = new ArrayList<>();
            return;
        }

        PathfindingStrategy pathfindingStrategy = new PathfindingStrategy(world);
        Tile start = world.getTile((int) actor.getX(), (int) actor.getY());
        Tile end = world.getTile(cursorX, cursorY);
        possiblePath = pathfindingStrategy.find(start, end);
    }

    public void onKeyPressed(Integer keyCode) {
        boolean primaryPressed = keyCode == Keyboard.PRIMARY;
        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();

        if (hoveringOnEmptyTile && primaryPressed) {
            // TODO: This should really be a move command
//                    game.getAudio().play("select.wav");
            ActorMoved.event.fire(new ActorMoved(actor, possiblePath));
            possiblePath = new ArrayList<>();
        }
    }

    public void onRender(Graphics2D graphics) {
        if (possiblePath.isEmpty()) {
            return;
        }

        int tileSize = 32;
        // Draw the path
        Stroke stroke = graphics.getStroke();
        graphics.setColor(Color.ORANGE);
        graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Tile start = possiblePath.get(0);

        float turtleTileX = start.getX();
        float turtleTileY = start.getY();

        for (Tile tile : possiblePath) {
            int tileX = tile.getX();
            int tileY = tile.getY();

            boolean isVertical = turtleTileX == tileX;
            boolean isHorizontal = turtleTileY == tileY;

            int turtleX = (int) (turtleTileX * tileSize);
            int turtleY = (int) (turtleTileY * tileSize);

            if (isVertical) {
                int centerX = turtleX + (tileSize / 2);
                int startY = turtleY + (tileSize / 2);
                int endY = (tileY * tileSize) + (tileSize / 2);

                graphics.drawLine(centerX, startY, centerX, endY);
            }

            if (isHorizontal) {
                int centerY = turtleY + (tileSize / 2);
                int startX = turtleX + (tileSize / 2);
                int endX = (tileX * tileSize) + (tileSize / 2);

                graphics.drawLine(startX, centerY, endX, centerY);
            }

            turtleTileX = tileX;
            turtleTileY = tileY;
        }
        graphics.setStroke(stroke);
    }
}

package game.state.battle.world;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Tile {
    private final int x;
    private final int y;
    private final boolean passable;

    public Tile(int x, int y, boolean passable) {
        this.x = x;
        this.y = y;
        this.passable = passable;
    }

    public boolean isPassable() {
        return passable;
    }

    public abstract void onRender(Graphics2D graphics);

    public static void drawOutline(List<Tile> tiles, Graphics2D graphics, Color color) {
        for (Tile tile : tiles) {
            List<Tile> neighbors = tile.getNeighbors(tiles);
            boolean hasAbove = neighbors.stream().anyMatch(neighbor -> neighbor.getY() < tile.getY());
            boolean hasBelow = neighbors.stream().anyMatch(neighbor -> neighbor.getY() > tile.getY());
            boolean hasLeft = neighbors.stream().anyMatch(neighbor -> neighbor.getX() < tile.getX());
            boolean hasRight = neighbors.stream().anyMatch(neighbor -> neighbor.getX() > tile.getX());

            Stroke oldStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(2));
            graphics.setColor(color);

            int tileX = tile.getX() * 32;
            int tileY = tile.getY() * 32;
            int tileWidth = 32;
            int tileHeight = 32;

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

    public List<Tile> getNeighbors(List<Tile> tiles) {
        Optional<Tile> above = tiles.stream().filter(tile -> tile.getX() == x && tile.getY() == y - 1).findFirst();
        Optional<Tile> below = tiles.stream().filter(tile -> tile.getX() == x && tile.getY() == y + 1).findFirst();
        Optional<Tile> left = tiles.stream().filter(tile -> tile.getX() == x - 1 && tile.getY() == y).findFirst();
        Optional<Tile> right = tiles.stream().filter(tile -> tile.getX() == x + 1 && tile.getY() == y).findFirst();
        return Arrays.asList(above, below, left, right).stream().filter(Optional::isPresent).map(Optional::get).collect(
                Collectors.toList());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static void drawTurtle(List<Tile> tiles, Graphics2D graphics, Color color) {
        if (tiles.isEmpty()) {
            return;
        }

        int tileSize = 32;
        // Draw the path
        Stroke stroke = graphics.getStroke();
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Tile start = tiles.get(0);

        float turtleTileX = start.getX();
        float turtleTileY = start.getY();

        for (Tile tile : tiles) {
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

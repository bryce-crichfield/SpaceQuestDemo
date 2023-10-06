package game.battle;

import javax.xml.datatype.Duration;
import java.awt.*;
import java.util.List;
import java.util.*;

public class TileMap {
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[width][height];

        // Initialize the tiles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((x + y) % 2 == 0) {
                    tiles[x][y] = new ColorTile(x, y, true, Color.WHITE);
                } else {
                    tiles[x][y] = new ColorTile(x, y, true, Color.BLACK);
                }
            }
        }
//
//        wall(3, 3);
//        wall(3, 4);
//        wall(3, 5);
//        wall(3, 6);
//
//        wall(5, 3);
//        wall(5, 4);
//        wall(5, 5);
//        wall(5, 6);
//
//        wall(7, 3);
//        wall(7, 4);
//        wall(7, 5);
    }

    public void wall(int x, int y) {
        tiles[x][y] = new ColorTile(x, y, false, Color.RED.darker().darker().darker());
    }

    public void onUpdate(Duration duration) {

    }

    public void onRender(Graphics2D graphics) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].onRender(graphics);
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[] getNeighbors(int x, int y) {
        List<Tile> neighbors = new ArrayList<>();

        // Up
        if (y > 0) {
            neighbors.add(tiles[x][y - 1]);
        }

        // Down
        if (y < height - 1) {
            neighbors.add(tiles[x][y + 1]);
        }

        // Left
        if (x > 0) {
            neighbors.add(tiles[x - 1][y]);
        }

        // Right
        if (x < width - 1) {
            neighbors.add(tiles[x + 1][y]);
        }

        return neighbors.toArray(new Tile[0]);
    }


}

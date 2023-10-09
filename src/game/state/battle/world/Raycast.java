package game.state.battle.world;

import java.util.ArrayList;
import java.util.List;

public class Raycast {
    int startX;
    int startY;
    int endX;
    int endY;

    Tile[][] tiles;
    List<Tile> foundTiles;

    public Raycast(Tile[][] tiles, int startX, int startY, int endX, int endY) {
        this.tiles = tiles;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        foundTiles = calculate();
    }

    private List<Tile> calculate() {
        List<Tile> foundTiles = new ArrayList<>();
        int x = startX;
        int y = startY;
        int dx = endX - startX;
        int dy = endY - startY;
        int stepX = dx < 0 ? -1 : 1;
        int stepY = dy < 0 ? -1 : 1;
        int longest = Math.abs(dx);
        int shortest = Math.abs(dy);
        boolean inverted = false;
        if (longest < shortest) {
            longest = Math.abs(dy);
            shortest = Math.abs(dx);
            inverted = true;
        }
        int numerator = longest >> 1;

        for (int i = 0; i <= longest; i++) {
            foundTiles.add(tiles[x][y]);
            numerator += shortest;
            if (numerator >= longest) {
                numerator -= longest;
                x += stepX;
                y += stepY;
            } else {
                if (inverted) {
                    y += stepY;
                } else {
                    x += stepX;
                }
            }
        }

        return foundTiles;
    }

    public List<Tile> getTiles() {
        return foundTiles;
    }


}

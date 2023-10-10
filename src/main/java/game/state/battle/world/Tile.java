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
}

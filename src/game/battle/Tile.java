package game.battle;

import java.awt.*;

public abstract class Tile {
    private final int x;
    private final int y;
    private final boolean passable;

    public Tile(int x, int y, boolean passable) {
        this.x = x;
        this.y = y;
        this.passable = passable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    boolean isPassable() {
        return passable;
    }

    public abstract void onRender(Graphics2D graphics);
}

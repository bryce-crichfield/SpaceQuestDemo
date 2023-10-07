package game.battle;

import game.Util;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class Actor {
    final float stepDuration = 0.35f;
    private final Color color;
    float x;
    float y;
    float targetX;
    float targetY;
    List<Tile> path = List.of();
    float walkTime;
    boolean selected = false;

    public Actor(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.color = color;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void onUpdate(Duration duration) {
        float dt = Util.perSecond(duration);

        // Advance the walk timer, and if we have a path, and the timer is up, move to the next tile
        walkTime += dt;
        if (path != null && !path.isEmpty()) {
            if (walkTime > stepDuration) {
                walkTime = 0;
                Tile next = path.remove(0);
                targetX = next.getX();
                targetY = next.getY();
            }
        }


        // Otherwise ease towards the target, and if we are close enough, snap to the target
        x = Util.easeIn(x, targetX, stepDuration, walkTime);
        y = Util.easeIn(y, targetY, stepDuration, walkTime);

        if (Math.abs(targetX - x) < 0.1) {
            x = targetX;
        }

        if (Math.abs(targetY - y) < 0.1) {
            y = targetY;
        }

    }

    public void onRender(Graphics2D graphics) {
        Color color = selected ? Color.GREEN : this.color;
        graphics.setColor(color);
        graphics.fillOval((int) (x * 32), (int) (y * 32), 32, 32);
        graphics.setColor(Color.BLACK);
        graphics.drawOval((int) (x * 32), (int) (y * 32), 32, 32);
    }

    public List<Tile> getPath() {
        return path;
    }

    public void setPath(List<Tile> path) {
        this.path = path;
    }
}

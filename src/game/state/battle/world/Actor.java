package game.state.battle.world;

import game.event.EventListener;
import game.state.battle.event.ActorMoved;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ActorSelected;
import game.util.Util;

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
    float health = 50;
    List<Tile> path = List.of();
    private final EventListener<ActorMoved> moveActorEventListener = event -> {
        System.out.println("Actor received pathfinding event");
        if (event.actor.equals(Actor.this)) {
            path = event.movePath;
        }
    };
    private float walkTime;
    private boolean selected = false;

    private final EventListener<ActorSelected> selectedEventListener = event -> {
        if (event.actor.equals(Actor.this)) {
            selected = true;
        }
    };
    private final EventListener<ActorDeselected> deselectedEventListener = event -> {
        if (event.actor.equals(Actor.this)) {
            selected = false;
        }
    };

    public Actor(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.color = color;
    }

    public EventListener<ActorSelected> getSelectedEventListener() {
        return selectedEventListener;
    }

    public EventListener<ActorDeselected> getDeselectedEventListener() {
        return deselectedEventListener;
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

        // draw the health bar
        float healthPercentage = health / 100;
        graphics.setColor(Color.RED);
        int healthWidth = (int) ((32 - 10) * healthPercentage);
        int healthHeight = 5;
        int healthX = (int) ((x * 32) + 5);
        int healthY = (int) ((y * 32) + 32 - 5);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(healthX, healthY, 32 - 10, healthHeight);
        graphics.setColor(Color.RED);
        graphics.fillRect(healthX, healthY, healthWidth, healthHeight);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(healthX, healthY, 32 - 10, healthHeight);
    }

    public EventListener<ActorMoved> getMoveActorEventListener() {
        return moveActorEventListener;
    }
}

package game.state.battle.world;

import game.state.battle.event.*;
import game.util.Util;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class Actor {
    final float stepDuration = 0.35f;
    private final Color color;
    private final int walkDistance = 5;
    private final int attackDistance = 3;
    float x;
    float y;
    float targetX;
    float targetY;
    String name = "Actor";
    float attack = 10;
    float health = 100;
    List<Tile> path = List.of();
    private float walkTime;
    private boolean selected = false;

    public Actor(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.color = color;
    }

    public int getAttackDistance() {
        return attackDistance;
    }

    public String getName() {
        return name;
    }

    public float getAttack() {
        return attack;
    }

    public float getHealth() {
        return health;
    }

    public void setWalkTime(float walkTime) {
        this.walkTime = walkTime;
    }

    public int getWalkDistance() {
        return walkDistance;
    }

    public void onActorMoved(ActorMoved event) {
        if (event.actor.equals(this)) {
            path = event.movePath;
        }
    }

    public void onActorSelected(ActorSelected event) {
        if (event.actor.equals(this)) {
            selected = true;
        }
    }

    public void onActorDeselected(ActorDeselected event) {
        if (event.actor.equals(this)) {
            selected = false;
        }
    }

    public void onActorAttacked(ActorAttacked attack) {
        if (attack.getAttacker().equals(this)) {
            return;
        }

        for (Tile tile : attack.getTargets()) {
            if (tile.getX() == x && tile.getY() == y) {
                System.out.println("Actor hit!");
                health -= 15;
                ActorDamaged.event.fire(this);

                if (health <= 0) {
                    ActorKilled.event.fire(new ActorKilled(this));
                }
            }
        }
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
                ActorAnimation.event.fire(this);
            }
        }

        // Otherwise ease towards the target, and if we are close enough, snap to the target
        x = Util.easeIn(x, targetX, stepDuration, walkTime);
        y = Util.easeIn(y, targetY, stepDuration, walkTime);
        ActorAnimation.event.fire(this);

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
        Color healthColor = healthPercentage > 0.5 ? Color.GREEN : healthPercentage > 0.25 ? Color.YELLOW : Color.RED;
        graphics.setColor(healthColor);
        int healthWidth = (int) ((32 - 10) * healthPercentage);
        int healthHeight = 5;
        int healthX = (int) ((x * 32) + 5);
        int healthY = (int) ((y * 32) + 32 - 5);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(healthX, healthY, 32 - 10, healthHeight);
        graphics.setColor(healthColor);
        graphics.fillRect(healthX, healthY, healthWidth, healthHeight);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(healthX, healthY, 32 - 10, healthHeight);
    }
}

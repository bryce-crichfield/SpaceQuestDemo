package game.state.battle.model.actor;

import game.character.GameCharacter;
import game.character.StarTrooper;
import game.state.battle.event.*;
import game.state.battle.model.world.Tile;
import game.state.battle.util.Cursor;

import java.awt.*;
import java.time.Duration;

public class Actor {
    int tileX;
    int tileY;

    ActorAnimation animation;
    GameCharacter character = StarTrooper.create();
    float currentHealthPoints = character.getStats().getHealth();
    float currentMovementPoints = character.getStats().getSpeed();
    Color color;
    private boolean selected = false;
    private boolean hovered = false;

    private boolean waiting = false;
    public Actor(int x, int y, Color color) {
        this.tileX = x;
        this.tileY = y;
        this.color = color;

        animation = new ActorAnimation(this);
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public int getAttackDistance() {
        return character.getStats().getRange();
    }

    public String getName() {
        return character.getName();
    }

    public float getHealth() {
        return currentHealthPoints;
    }

    public void onActorMoved(ActionActorMoved event) {
        if (event.actor.equals(this)) {
//            currentMovementPoints -= event.movePath.size();
            animation.start(event.movePath);
        }
    }

    public void onCursorMoved(Cursor cursor) {
        boolean cursorHovers = cursor.getCursorX() == this.tileX && cursor.getCursorY() == this.tileY;

        if (hovered && !cursorHovers) {
            hovered = true;
            ActorHovered.event.fire(this);
        } else if (!hovered && cursorHovers) {
            hovered = false;
            ActorUnhovered.event.fire(this);
        }
    }

    public void onActorSelected(Actor actor) {
        if (actor.equals(this)) {
            selected = true;
        }
    }

    public void onActorDeselected(Actor actor) {
        if (actor.equals(this)) {
            selected = false;
        }
    }

    public void onActorAttacked(ActionActorAttack attack) {
        if (attack.getAttacker().equals(this)) {
            return;
        }

        for (Tile tile : attack.getTargets()) {
            if (tile.getX() == tileX && tile.getY() == tileY) {
                currentHealthPoints -= attack.getAttacker().getAttack();
                ActorDamaged.event.fire(this);

                if (currentHealthPoints <= 0) {
                    ActorKilled.event.fire(this);
                }
            }
        }
    }

    public float getAttack() {
        return character.getStats().getPhysical();
    }

    public float getX() {
        return tileX;
    }

    public float getY() {
        return tileY;
    }

    public void onUpdate(Duration duration) {
        animation.onUpdate(duration);
        tileX = (int) animation.getX();
        tileY = (int) animation.getY();
    }

    public void onRender(Graphics2D graphics) {
        // Draw the actor
        Color color = selected ? Color.GREEN : waiting ? Color.GRAY : this.color;
        float x = animation.getX();
        float y = animation.getY();
        graphics.setColor(color);
        graphics.fillOval((int) (x * 32), (int) (y * 32), 32, 32);
        graphics.setColor(Color.BLACK);
        graphics.drawOval((int) (x * 32), (int) (y * 32), 32, 32);

        // Draw the health bar
        float healthPercentage = currentHealthPoints / character.getStats().getHealth();
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

    public int getMovementPoints() {
        return (int) currentMovementPoints;
    }
}

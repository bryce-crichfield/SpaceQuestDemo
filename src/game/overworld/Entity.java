package game.overworld;

import game.Util;

import java.awt.*;
import java.time.Duration;

public abstract class Entity {
    float x;
    float y;
    float velocityX;
    float velocityY;
    float accelerationX;
    float accelerationY;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void onUpdate(Duration delta) {
        float dt = Util.perSecond(delta);

        velocityX += accelerationX * dt;
        velocityY += accelerationY * dt;

        x += velocityX * dt;
        y += velocityY * dt;

        accelerationX = 0;
        accelerationY = 0;

        velocityX *= 0.9f;
        velocityY *= 0.9f;
    }

    public abstract void onRender(Graphics2D graphics);
}

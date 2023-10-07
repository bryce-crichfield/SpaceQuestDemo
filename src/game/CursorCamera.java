package game;

import game.battle.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class CursorCamera {
    public int cursorX;
    public int cursorY;
    float velocityX;
    float velocityY;
    float accelerationX;
    float accelerationY;
    Camera camera;
    Keyboard keyboard;
    int tileSize;

    public CursorCamera(Camera camera, Keyboard keyboard, int tileSize) {
        this.camera = camera;
        this.keyboard = keyboard;
        this.tileSize = tileSize;

        cursorX = 0;
        cursorY = 0;
        velocityX = 0;
        velocityY = 0;
        accelerationX = 0;
        accelerationY = 0;
    }

    public boolean onUpdate(Duration duration, TileMap tileMap) {
        float dt = Util.perSecond(duration);

        int cursorWorldX = cursorX * tileSize;
        int cursorWorldY = cursorY * tileSize;


        velocityX = (cursorWorldX - camera.getX()) * 10;
        velocityY = (cursorWorldY - camera.getY()) * 10;

        camera.setX(camera.getX() + (velocityX * dt));
        camera.setY(camera.getY() + (velocityY * dt));

        velocityX *= 0.9;
        velocityY *= 0.9;

        boolean cursorChanged = false;

        if (keyboard.pressed(Keyboard.LEFT)) {
            cursorX--;
            cursorChanged = true;
        }

        if (keyboard.pressed(Keyboard.RIGHT)) {
            cursorX++;
            cursorChanged = true;
        }

        if (keyboard.pressed(Keyboard.UP)) {
            cursorY--;
            cursorChanged = true;
        }

        if (keyboard.pressed(Keyboard.DOWN)) {
            cursorY++;
            cursorChanged = true;
        }

        if (keyboard.pressed(KeyEvent.VK_MINUS)) {
            float zoom = camera.getZoom();
            zoom = Math.max(zoom - 0.25f, 0.25f);
            camera.setZoom(zoom);
        }

        if (keyboard.pressed(KeyEvent.VK_EQUALS)) {
            float zoom = camera.getZoom();
            zoom = Math.min(zoom + 0.25f, 2);
            camera.setZoom(zoom);
        }

        // clamp
        cursorX = Util.clamp(cursorX, 0, tileMap.getWidth() - 1);
        cursorY = Util.clamp(cursorY, 0, tileMap.getHeight() - 1);

        return cursorChanged;
    }

    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(2));
        graphics.drawRect(cursorX * tileSize, cursorY * tileSize, tileSize, tileSize);
    }

    public int getCursorX() {
        return cursorX;
    }

    public void setCursorX(int cursorX) {
        this.cursorX = cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    public void setCursorY(int cursorY) {
        this.cursorY = cursorY;
    }
}

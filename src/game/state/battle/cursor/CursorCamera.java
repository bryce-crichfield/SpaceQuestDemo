package game.state.battle.cursor;

import game.Game;
import game.event.Event;
import game.event.EventListener;
import game.io.Keyboard;
import game.state.battle.event.CursorMoved;
import game.state.battle.world.World;
import game.util.Camera;
import game.util.Util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class CursorCamera {
    private final Game game;
    private final float timerMax = .75f;
    public int cursorX;
    public int cursorY;
    float velocityX;
    float velocityY;
    float accelerationX;
    float accelerationY;
    Camera camera;
    private final EventListener<Integer> onKeyPressEventListener = keyCode -> {
        System.out.println("Keyboard Pressed");
        switch (keyCode) {
            case Keyboard.LEFT -> {
                cursorX--;
                CursorMoved.event.fire(new CursorMoved(this));
            }
            case Keyboard.RIGHT -> {
                cursorX++;
                CursorMoved.event.fire(new CursorMoved(this));

            }
            case Keyboard.UP -> {
                cursorY--;
                CursorMoved.event.fire(new CursorMoved(this));
            }
            case Keyboard.DOWN -> {
                cursorY++;
                CursorMoved.event.fire(new CursorMoved(this));

            }
            case KeyEvent.VK_MINUS -> {
                float zoom = camera.getZoom();
                zoom = Math.max(zoom - 0.25f, 0.25f);
                camera.setZoom(zoom);
                CursorMoved.event.fire(new CursorMoved(this));
            }
            case KeyEvent.VK_EQUALS -> {
                float zoom = camera.getZoom();
                zoom = Math.min(zoom + 0.25f, 2);
                camera.setZoom(zoom);
                CursorMoved.event.fire(new CursorMoved(this));
            }
        }
    };
    int tileSize;
    Event<CursorMoved> emitter;
    Mode mode = Mode.NORMAL;
    Color color = Color.RED;
    float timer = 0;
    public CursorCamera(Camera camera, Game game, World world) {
        this.camera = camera;
        this.tileSize = game.TILE_SIZE;
        this.game = game;

        cursorX = 0;
        cursorY = 0;
        velocityX = 0;
        velocityY = 0;
        accelerationX = 0;
        accelerationY = 0;

        CursorMoved.event.listenWith(event -> {
            cursorX = Util.clamp(cursorX, 0, world.getWidth() - 1);
            cursorY = Util.clamp(cursorY, 0, world.getHeight() - 1);
            game.getAudio().play("beep.wav");
        });
    }

    public EventListener<Integer> getOnKeyPressEventListener() {
        return onKeyPressEventListener;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void enterBlinkingMode() {
        mode = Mode.BLINKING;
    }

    public void enterDilatedMode() {
        mode = Mode.DILATED;
    }

    public void enterNormalMode() {
        mode = Mode.NORMAL;
    }

    public void onUpdate(Duration duration) {
        updateCameraKinematics(duration);
    }

    private void updateCameraKinematics(Duration duration) {
        float dt = Util.perSecond(duration);

        if (mode == Mode.BLINKING) {
            timer += dt;
            timer = Util.wrap(timer, 0, timerMax);
        }

        int cursorWorldX = cursorX * tileSize;
        int cursorWorldY = cursorY * tileSize;

        velocityX = (cursorWorldX - camera.getX()) * 10;
        velocityY = (cursorWorldY - camera.getY()) * 10;

        camera.setX(camera.getX() + (velocityX * dt));
        camera.setY(camera.getY() + (velocityY * dt));

        velocityX *= 0.9;
        velocityY *= 0.9;
    }

    public void onRender(Graphics2D graphics) {
        int offset = 0;
        if ((mode == Mode.BLINKING || mode == Mode.DILATED) && timer < timerMax / 2) {
            offset = 5;
        }

        int size = tileSize + offset;
        int x = cursorX * tileSize - offset / 2;
        int y = cursorY * tileSize - offset / 2;

        Stroke oldStroke = graphics.getStroke();
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect(x, y, size, size);
        graphics.setStroke(oldStroke);
        graphics.setColor(color);
        graphics.drawRect(x, y, size, size);
    }

    public int getCursorX() {
        return cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    enum Mode {
        BLINKING,
        DILATED,
        NORMAL
    }
}

package game.battle.cursor;

import game.Camera;
import game.Game;
import game.Keyboard;
import game.Util;
import game.battle.world.World;
import game.event.EventEmitter;
import game.event.EventSource;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class CursorCamera implements EventSource<CursorEvent> {
    public int cursorX;
    public int cursorY;
    float velocityX;
    float velocityY;
    float accelerationX;
    float accelerationY;
    Camera camera;
    Keyboard keyboard;
    int tileSize;
    EventEmitter<CursorEvent> emitter;

    enum Mode {
        BLINKING,
        DILATED,
        NORMAL
    };

    Mode mode = Mode.NORMAL;

    public void setColor(Color color) {
        this.color = color;
    }

    Color color = Color.RED;

    float timer = 0;
    final float timerMax = .75f;
    private final Game game;

    public void enterBlinkingMode() {
        mode = Mode.BLINKING;
    }

    public void enterDilatedMode() {
        mode = Mode.DILATED;
    }

    public void enterNormalMode() {
        mode = Mode.NORMAL;
    }

    public CursorCamera(Camera camera, Keyboard keyboard, int tileSize, Game game) {
        this.camera = camera;
        this.keyboard = keyboard;
        this.tileSize = tileSize;
        this.game = game;

        cursorX = 0;
        cursorY = 0;
        velocityX = 0;
        velocityY = 0;
        accelerationX = 0;
        accelerationY = 0;

        emitter = new EventEmitter<>();
    }

    public void onUpdate(Duration duration, World world) {
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
        cursorX = Util.clamp(cursorX, 0, world.getWidth() - 1);
        cursorY = Util.clamp(cursorY, 0, world.getHeight() - 1);


        if (cursorChanged) {
            fireEvent(new CursorEvent(this));
            game.getAudio().play("beep.wav");
        }
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

    @Override
    public EventEmitter getEmitter() {
        return emitter;
    }

}

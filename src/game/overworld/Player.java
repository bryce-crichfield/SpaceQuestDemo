package game.overworld;

import game.Game;
import game.Keyboard;

import java.awt.*;
import java.time.Duration;

public class Player extends Entity {
    Game game;
    public Player(float x, float y, Game game) {
        super(x, y);
        this.game = game;
    }

    @Override
    public void onUpdate(Duration dt) {
        int speed = (int) ((int) 2 * 1e4);

        if (game.getKeyboard().down(Keyboard.UP)) {
            accelerationY = -speed;
        }

        if (game.getKeyboard().down(Keyboard.DOWN)) {
            accelerationY = speed;
        }

        if (game.getKeyboard().down(Keyboard.LEFT)) {
            accelerationX = -speed;
        }

        if (game.getKeyboard().down(Keyboard.RIGHT)) {
            accelerationX = speed;
        }
        super.onUpdate(dt);

    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect((int) x, (int) y, 32, 32);
    }
}

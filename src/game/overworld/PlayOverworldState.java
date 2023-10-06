package game.overworld;

import game.Camera;
import game.Game;
import game.GameState;
import game.Util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.Duration;

public class PlayOverworldState extends GameState {
    Camera camera;
    Player player;
    Entity entity;

    public PlayOverworldState(Game game) {
        super(game);

        camera = new Camera(game);
        player = new Player(0, 0, game);
        entity = new Player(10, 10, game);
    }

    @Override
    public void onUpdate(Duration delta) {
        player.onUpdate(delta);

        float dt = Util.perSecond(delta);

        // lerp the camera towards the player
        float cameraX = Util.lerp(camera.getX(), player.getX(), dt);
        float cameraY = Util.lerp(camera.getY(), player.getY(), dt);
        camera.setX(cameraX);
        camera.setY(cameraY);

        if (getGame().getKeyboard().pressed(KeyEvent.VK_ESCAPE)) {
            getGame().popState();
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.DARK_GRAY.darker().darker().darker());
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        AffineTransform restore = graphics.getTransform();
        AffineTransform cameraTransform = camera.getTransform();
        graphics.transform(cameraTransform);

        player.onRender(graphics);
        entity.onRender(graphics);

        graphics.setTransform(restore);
    }
}

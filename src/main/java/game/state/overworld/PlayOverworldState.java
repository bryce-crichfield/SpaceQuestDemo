package game.state.overworld;

import game.Game;
import game.state.GameState;
import game.state.title.StarBackground;
import game.util.Camera;
import game.util.Util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.Duration;

public class PlayOverworldState extends GameState {
    StarBackground starBackground;
    Camera camera;
    Player player;
    Entity entity;

    public PlayOverworldState(Game game) {
        super(game);

        camera = new Camera(game);
        player = new Player(0, 0, game);
        entity = new Player(10, 10, game);

        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);

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
        graphics.setColor(new Color(0x0A001A));
        graphics.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        starBackground.onRender(graphics);

        AffineTransform restore = graphics.getTransform();
        AffineTransform cameraTransform = camera.getTransform();
        graphics.transform(cameraTransform);

        player.onRender(graphics);
        entity.onRender(graphics);

        graphics.setTransform(restore);
    }
}

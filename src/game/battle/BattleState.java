package game.battle;

import game.Camera;
import game.Game;
import game.GameState;
import game.battle.cursor.CursorCamera;
import game.battle.cursor.CursorEvent;
import game.battle.pathfinding.PathfindingManager;
import game.battle.selection.SelectionManager;
import game.battle.world.Actor;
import game.battle.world.Tile;
import game.battle.world.World;
import game.event.Event;
import game.event.EventListener;
import game.title.StarBackground;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BattleState extends GameState {
    StarBackground starBackground;
    Camera camera;
    CursorCamera cursorCamera;
    World world;
    List<Tile> path;

    SelectionManager selectionManager;
    PathfindingManager pathfindingManager;

    public BattleState(Game game) {
        super(game);
        camera = new Camera(game);
        cursorCamera = new CursorCamera(camera, game.getKeyboard(), 32);
        world = new World(8, 8);
        path = new ArrayList<>();

        starBackground = new StarBackground(this, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        // Set up the selection manager and register the actors as listeners
        selectionManager = new SelectionManager(game.getKeyboard(), world);
        cursorCamera.addListener(selectionManager);
        for (Actor actor : world.getActors()) {
            selectionManager.addListener(actor.getSelectionEventListener());
        }

        // Set up the pathfinding manager and register the actors as listeners
        pathfindingManager = new PathfindingManager(selectionManager, game.getKeyboard(), world, game);
        cursorCamera.addListener(pathfindingManager.getCursorEventListener());
        selectionManager.addListener(pathfindingManager.getSelectionEventListener());
        for (Actor actor : world.getActors()) {
            pathfindingManager.addListener(actor.getPathfindingListener());
        }
    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);

        cursorCamera.onUpdate(delta, world);
        pathfindingManager.onUpdate();
        selectionManager.onUpdate();

        world.onUpdate(delta);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.DARK_GRAY.darker().darker().darker());
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        starBackground.onRender(graphics);

        AffineTransform restore = graphics.getTransform();
        AffineTransform transform = camera.getTransform();
        graphics.setTransform(transform);

        world.onRender(graphics);
        cursorCamera.onRender(graphics);
        pathfindingManager.onRender(graphics);

        graphics.setTransform(restore);

        // TODO: Draw the action menu
    }
}

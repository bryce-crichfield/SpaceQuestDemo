package game.battle;

import game.Camera;
import game.Game;
import game.GameState;
import game.battle.cursor.CursorCamera;
import game.battle.pathfinding.PathfindingManager;
import game.battle.selection.DeselectedEvent;
import game.battle.selection.SelectedEvent;
import game.battle.selection.SelectionEvent;
import game.battle.selection.SelectionManager;
import game.battle.world.Actor;
import game.battle.world.Tile;
import game.battle.world.World;
import game.event.EventListener;
import game.title.StarBackground;
import widget.ButtonWidget;
import widget.Menu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BattleState extends GameState {
    StarBackground starBackground;
    Camera camera;
    CursorCamera cursorCamera;
    World world;
    List<Tile> path;
    SelectionManager selectionManager;
    PathfindingManager pathfindingManager;
    Menu actionMenu;
    EventListener<SelectionEvent> menuSelectionEventListener;
    ActionMode currentActionMode = new ObserverMode();

    void enterObserverMode() {
        currentActionMode = new ObserverMode();
        cursorCamera.enterDilatedMode();
        cursorCamera.setColor(Color.WHITE);
    }

    void enterSelectMode() {
        currentActionMode = new SelectActionMode();
        actionMenu.setVisible(true);
        cursorCamera.enterBlinkingMode();
        cursorCamera.setColor(Color.WHITE);
    }

    void enterAttackMode() {
        currentActionMode = new AttackActionMode();
        cursorCamera.enterBlinkingMode();
        cursorCamera.setColor(Color.RED);
    }

    void enterMoveMode() {
        currentActionMode = new MoveActionMode();
        cursorCamera.enterBlinkingMode();
        cursorCamera.setColor(Color.ORANGE);
    }

    public BattleState(Game game) {
        super(game);
        camera = new Camera(game);
        cursorCamera = new CursorCamera(camera, game.getKeyboard(), 32, game);
        world = new World(16, 16);
        path = new ArrayList<>();

        starBackground = new StarBackground(this, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        int menuWidth = 3 * getGame().TILE_SIZE;
        int menuHeight = 3 * getGame().TILE_SIZE;
        int menuX = getGame().SCREEN_WIDTH - menuWidth - getGame().TILE_SIZE;
        int menuY = getGame().SCREEN_HEIGHT - menuHeight - getGame().TILE_SIZE;
        actionMenu = new Menu(getGame(), menuX, menuY, menuWidth, menuHeight);
        actionMenu.setWidgets(
                new ButtonWidget("Attack", getGame(), this::enterAttackMode),
                new ButtonWidget("Move", getGame(), this::enterMoveMode)
        );

        menuSelectionEventListener = event -> {
            if (event instanceof SelectedEvent selectedEvent) {
                game.getAudio().play("select.wav");
                enterSelectMode();
            }

            if (event instanceof DeselectedEvent deselectedEvent) {
                enterObserverMode();
            }
        };

        actionMenu.getEmitter().addListener(closeEvent -> {
            currentActionMode = new ObserverMode();
            selectionManager.deselectActor();
        });

        // Set up the selection manager and register the actors as listeners
        selectionManager = new SelectionManager(game.getKeyboard(), world);
        cursorCamera.addListener(selectionManager.getCursorEventListener());
        selectionManager.addListener(menuSelectionEventListener);
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

        this.enterObserverMode();


    }

    public void drawWithCamera(Graphics2D graphics, Consumer<Graphics2D> draw) {
        AffineTransform restore = graphics.getTransform();
        AffineTransform transform = camera.getTransform();
        graphics.setTransform(transform);
        draw.accept(graphics);
        graphics.setTransform(restore);
    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);

        currentActionMode.onUpdate(delta);

        world.onUpdate(delta);

        if (getGame().getKeyboard().pressed(KeyEvent.VK_ESCAPE)) {
            getGame().popState();
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(new Color(0x0A001A));
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        starBackground.onRender(graphics);

        AffineTransform restore = graphics.getTransform();
        AffineTransform transform = camera.getTransform();
        graphics.setTransform(transform);
        world.onRender(graphics);
        graphics.setTransform(restore);

        currentActionMode.onRender(graphics);
    }
    interface ActionMode {
        void onUpdate(Duration delta);

        void onRender(Graphics2D graphics);
    }

    class ObserverMode implements ActionMode {
        @Override
        public void onUpdate(Duration delta) {
            cursorCamera.onUpdate(delta, world);
            selectionManager.onUpdate();
        }

        @Override
        public void onRender(Graphics2D graphics) {
            drawWithCamera(graphics, camera -> {
                cursorCamera.onRender(camera);
            });
        }
    }

    class SelectActionMode implements ActionMode {
        @Override
        public void onUpdate(Duration delta) {
            actionMenu.onUpdate(delta);
        }

        @Override
        public void onRender(Graphics2D graphics) {
            drawWithCamera(graphics, camera -> {
                cursorCamera.onRender(camera);
            });
            actionMenu.onRender(graphics);
        }
    }

    class AttackActionMode implements ActionMode {
        @Override
        public void onUpdate(Duration delta) {
            cursorCamera.onUpdate(delta, world);
            selectionManager.onUpdate();
        }

        @Override
        public void onRender(Graphics2D graphics) {
            drawWithCamera(graphics, camera -> {
                cursorCamera.onRender(camera);
            });
        }
    }

    class MoveActionMode implements ActionMode {
        @Override
        public void onUpdate(Duration delta) {
            cursorCamera.onUpdate(delta, world);
            pathfindingManager.onUpdate();
            selectionManager.onUpdate();
        }

        @Override
        public void onRender(Graphics2D graphics) {
            drawWithCamera(graphics, camera -> {
                cursorCamera.onRender(camera);
                pathfindingManager.onRender(camera);
            });
        }
    }
}

package game.state.battle;

import game.event.EventListener;
import game.state.battle.world.Raycast;
import game.util.Camera;
import game.Game;
import game.state.GameState;
import game.state.battle.cursor.CursorCamera;
import game.state.battle.pathfinding.PathfindingManager;
import game.state.battle.selection.DeselectedEvent;
import game.state.battle.selection.SelectedEvent;
import game.state.battle.selection.SelectionManager;
import game.state.battle.world.Actor;
import game.state.battle.world.Tile;
import game.state.battle.world.World;
import game.state.title.StarBackground;
import game.widget.ButtonWidget;
import game.widget.Menu;

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

        EventListener<SelectedEvent> onSelectedEventListener = event -> {
            game.getAudio().play("select.wav");
            enterSelectMode();
        };

        EventListener<DeselectedEvent> onDeselectedEventListener = event -> {
            enterObserverMode();
        };

        actionMenu.getOnCloseEvent().listenWith(closeEvent -> {
            currentActionMode = new ObserverMode();
            selectionManager.deselectActor();
        });

        // Set up the selection manager and register the actors as listeners
        selectionManager = new SelectionManager(game.getKeyboard(), world);
        cursorCamera.getOnCursorEvent().listenWith(selectionManager.getCursorEventListener());
        selectionManager.getOnSelectedEvent().listenWith(event -> {
            game.getAudio().play("select.wav");
            enterSelectMode();
        });

        selectionManager.getOnDeselectedEvent().listenWith(event -> {
            enterObserverMode();
        });

        for (Actor actor : world.getActors()) {
            selectionManager.getOnSelectedEvent().listenWith(actor.getSelectedEventListener());
            selectionManager.getOnDeselectedEvent().listenWith(actor.getDeselectedEventListener());
        }

        // Set up the pathfinding manager and register the actors as listeners
        pathfindingManager = new PathfindingManager(selectionManager, game.getKeyboard(), world, game);
        cursorCamera.getOnCursorEvent().listenWith(pathfindingManager.getCursorEventListener());
        selectionManager.getOnSelectedEvent().listenWith(pathfindingManager.getSelectedEventListener());
        selectionManager.getOnDeselectedEvent().listenWith(pathfindingManager.getDeselectedEventListener());
        for (Actor actor : world.getActors()) {
            pathfindingManager.getOnMoveActorEvent().listenWith(actor.getMoveActorEventListener());
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
        Raycast raycast;


        @Override
        public void onUpdate(Duration delta) {
            cursorCamera.onUpdate(delta, world);
            selectionManager.onUpdate();

            if (selectionManager.getCurrentlySelectedActor().isPresent()) {
                Actor actor = selectionManager.getCurrentlySelectedActor().get();
                raycast = world.raycast(cursorCamera.getCursorX(), cursorCamera.getCursorY(), (int) actor.getX(),
                                        (int) actor.getY()
                );
            }
        }

        @Override
        public void onRender(Graphics2D graphics) {
            drawWithCamera(graphics, camera -> {
                cursorCamera.onRender(camera);

                if (raycast == null) {
                    return;
                }
                // Draw the raycast
                List<Tile> tiles = raycast.getTiles();
                for (Tile tile : tiles) {
                    boolean hasImmediateNeighborAbove = tiles.stream().findAny().filter(t -> t.getX() == tile.getX() && t.getY() == tile.getY() - 1).isPresent();
                    boolean hasImmediateNeighborBelow = tiles.stream().findAny().filter(t -> t.getX() == tile.getX() && t.getY() == tile.getY() + 1).isPresent();
                    boolean hasImmediateNeighborLeft = tiles.stream().findAny().filter(t -> t.getX() == tile.getX() - 1 && t.getY() == tile.getY()).isPresent();
                    boolean hasImmediateNeighborRight = tiles.stream().findAny().filter(t -> t.getX() == tile.getX() + 1 && t.getY() == tile.getY()).isPresent();

                    Stroke stroke = graphics.getStroke();
                    graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    graphics.setColor(Color.RED);
                    int tileX = tile.getX() * 32;
                    int tileY = tile.getY() * 32;
                    int tileSize = 32;

                    if (!hasImmediateNeighborAbove) {
                        graphics.drawLine(tileX, tileY, tileX + tileSize, tileY);
                    }

                    if (!hasImmediateNeighborBelow) {
                        graphics.drawLine(tileX, tileY + tileSize, tileX + tileSize, tileY + tileSize);
                    }

                    if (!hasImmediateNeighborLeft) {
                        graphics.drawLine(tileX, tileY, tileX, tileY + tileSize);
                    }

                    if (!hasImmediateNeighborRight) {
                        graphics.drawLine(tileX + tileSize, tileY, tileX + tileSize, tileY + tileSize);
                    }

                    graphics.setStroke(stroke);
                }


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

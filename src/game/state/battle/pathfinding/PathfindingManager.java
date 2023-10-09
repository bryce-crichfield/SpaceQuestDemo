package game.state.battle.pathfinding;

import game.Game;
import game.io.Keyboard;
import game.state.battle.cursor.CursorEvent;
import game.state.battle.selection.DeselectedEvent;
import game.state.battle.selection.SelectedEvent;
import game.state.battle.selection.SelectionEvent;
import game.state.battle.selection.SelectionManager;
import game.state.battle.world.Actor;
import game.state.battle.world.Tile;
import game.state.battle.world.World;
import game.event.EventEmitter;
import game.event.EventListener;
import game.event.EventSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PathfindingManager implements EventSource<PathfindingEvent> {
    private final EventEmitter<PathfindingEvent> emitter;
    private final SelectionManager selectionManager;
    private final Keyboard keyboard;
    private final World world;
    private final Game game;
    int cursorX = 0;
    int cursorY = 0;
    private final EventListener<CursorEvent> cursorEventListener = event -> {
        cursorX = event.cursorCamera.getCursorX();
        cursorY = event.cursorCamera.getCursorY();
    };
    private List<Tile> possiblePath;
    private final EventListener<SelectionEvent> selectionEventListener = event -> {
        if (event instanceof SelectedEvent selectedEvent) {
            possiblePath = new ArrayList<>();
        }

        if (event instanceof DeselectedEvent deselectedEvent) {
            possiblePath = new ArrayList<>();
        }
    };
    public PathfindingManager(SelectionManager selectionManager, Keyboard keyboard, World world, Game game) {
        this.selectionManager = selectionManager;
        this.keyboard = keyboard;
        this.world = world;
        this.game = game;
        possiblePath = new ArrayList<>();
        emitter = new EventEmitter<>();
    }

    public EventListener<SelectionEvent> getSelectionEventListener() {
        return selectionEventListener;
    }

    public EventListener<CursorEvent> getCursorEventListener() {
        return cursorEventListener;
    }

    public void onUpdate() {
        boolean primaryPressed = keyboard.pressed(Keyboard.PRIMARY);
        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();
        boolean actorSelected = selectionManager.getCurrentlySelectedActor().isPresent();

        if (hoveringOnEmptyTile && actorSelected) {
            Pathfinder pathfinder = new Pathfinder(world);
            Actor actor = selectionManager.getCurrentlySelectedActor().get();
            Tile start = world.getTile((int) actor.getX(), (int) actor.getY());
            Tile end = world.getTile(cursorX, cursorY);
            possiblePath = pathfinder.find(start, end);

            if (primaryPressed) {
                // TODO: This should really be a move command
                game.getAudio().play("select.wav");
                emitter.fireEvent(new PathfindingEvent(actor, possiblePath));
                possiblePath = new ArrayList<>();
            }
        }
    }

    public void onRender(Graphics2D graphics) {
        if (possiblePath.isEmpty()) {
            return;
        }

        int tileSize = game.TILE_SIZE;
        // Draw the path
        Stroke stroke = graphics.getStroke();
        graphics.setColor(Color.ORANGE);
        graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Tile start = possiblePath.get(0);

        float turtleTileX = start.getX();
        float turtleTileY = start.getY();

        for (Tile tile : possiblePath) {
            int tileX = tile.getX();
            int tileY = tile.getY();

            boolean isVertical = turtleTileX == tileX;
            boolean isHorizontal = turtleTileY == tileY;

            int turtleX = (int) (turtleTileX * tileSize);
            int turtleY = (int) (turtleTileY * tileSize);

            if (isVertical) {
                int centerX = turtleX + (tileSize / 2);
                int startY = turtleY + (tileSize / 2);
                int endY = (tileY * tileSize) + (tileSize / 2);

                graphics.drawLine(centerX, startY, centerX, endY);
            }

            if (isHorizontal) {
                int centerY = turtleY + (tileSize / 2);
                int startX = turtleX + (tileSize / 2);
                int endX = (tileX * tileSize) + (tileSize / 2);

                graphics.drawLine(startX, centerY, endX, centerY);
            }

            turtleTileX = tileX;
            turtleTileY = tileY;
        }
        graphics.setStroke(stroke);
    }

    @Override
    public EventEmitter<PathfindingEvent> getEmitter() {
        return emitter;
    }
}

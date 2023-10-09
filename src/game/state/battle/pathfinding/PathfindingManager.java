package game.state.battle.pathfinding;

import game.Game;
import game.event.EventListener;
import game.io.Keyboard;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ActorMoved;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ActorSelected;
import game.state.battle.selection.SelectionManager;
import game.state.battle.world.Actor;
import game.state.battle.world.Tile;
import game.state.battle.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathfindingManager {
    private final Keyboard keyboard;
    private final World world;
    private final Game game;
    int cursorX = 0;
    int cursorY = 0;
    private final EventListener<CursorMoved> cursorEventListener = event -> {
        cursorX = event.cursorCamera.getCursorX();
        cursorY = event.cursorCamera.getCursorY();
    };
    private List<Tile> possiblePath;
    private Optional<Actor> selectedActor = Optional.empty();
    private final EventListener<ActorSelected> selectedEventListener = event -> {
        selectedActor = Optional.of(event.actor);
        possiblePath = new ArrayList<>();
    };
    private final EventListener<ActorDeselected> deselectedEventListener = event -> {
        selectedActor = Optional.empty();
        possiblePath = new ArrayList<>();
    };

    public PathfindingManager(Keyboard keyboard, World world, Game game) {
        this.keyboard = keyboard;
        this.world = world;
        this.game = game;
        possiblePath = new ArrayList<>();
    }

    public EventListener<ActorSelected> getSelectedEventListener() {
        return selectedEventListener;
    }

    public EventListener<ActorDeselected> getDeselectedEventListener() {
        return deselectedEventListener;
    }

    public EventListener<CursorMoved> getCursorEventListener() {
        return cursorEventListener;
    }

    public void onUpdate() {
        boolean primaryPressed = keyboard.pressed(Keyboard.PRIMARY);
        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();
        boolean actorSelected = selectedActor.isPresent();

        if (hoveringOnEmptyTile && actorSelected) {
            Pathfinder pathfinder = new Pathfinder(world);

            Actor actor = selectedActor.get();
            Tile start = world.getTile((int) actor.getX(), (int) actor.getY());
            Tile end = world.getTile(cursorX, cursorY);
            possiblePath = pathfinder.find(start, end);

            if (primaryPressed) {
                // TODO: This should really be a move command
                game.getAudio().play("select.wav");
                ActorMoved.event.fire(new ActorMoved(actor, possiblePath));
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
}

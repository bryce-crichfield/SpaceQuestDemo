package game.battle;

import game.*;
import game.title.StarBackground;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayBattleState extends GameState {
    StarBackground starBackground;
    Camera camera;
    CursorCamera cursorCamera;
    TileMap tileMap;
    List<Tile> path;

    List<Actor> actors;
    Optional<Actor> selectedActor = Optional.empty();
    List<Tile> possiblePath = new ArrayList<>();

    boolean actionMenuOpen = true;


    public PlayBattleState(Game game) {
        super(game);
        camera = new Camera(game);
        cursorCamera = new CursorCamera(camera, game.getKeyboard(), 32);
        tileMap = new TileMap(8, 8);
        path = new ArrayList<>();
        actors = new ArrayList<>();
        actors.add(new Actor(3, 0, Color.RED));
        actors.add(new Actor(5, 0, Color.BLUE));
        actors.add(new Actor(6, 0, Color.MAGENTA));

        starBackground = new StarBackground(this, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);

        boolean cursorChanged = cursorCamera.onUpdate(delta, tileMap);

        updateActorSelection();

        if (cursorChanged)
            updatePossiblePath();

        for (Actor actor : actors) {
            actor.onUpdate(delta);
        }
    }

    private void updateActorSelection() {
        int cursorX = cursorCamera.getCursorX();
        int cursorY = cursorCamera.getCursorY();

        boolean primaryPressed = getGame().getKeyboard().pressed(Keyboard.PRIMARY);
        boolean secondaryPressed = getGame().getKeyboard().pressed(Keyboard.SECONDARY);
        Optional<Actor> hoveredActor = actors.stream().filter(
                actor -> actor.getX() == cursorX && actor.getY() == cursorY).findFirst();

        if (secondaryPressed) {
            selectedActor.ifPresent(actor -> actor.setSelected(false));
            selectedActor = Optional.empty();
            possiblePath = new ArrayList<>();
        }

        boolean hoveredOnEmptyTile = hoveredActor.isEmpty();
        boolean hoveredOnOtherActor = hoveredActor.isPresent() && (selectedActor.isEmpty() || !selectedActor.get().equals(
                hoveredActor.get()));

        if (hoveredOnOtherActor && primaryPressed) {
            selectedActor.ifPresent(actor -> actor.setSelected(false));

            hoveredActor.get().setSelected(true);
            selectedActor = hoveredActor;
        }

        if (selectedActor.isPresent() && hoveredOnEmptyTile && primaryPressed) {
            // submit the possible path to the actor and deselect
            selectedActor.get().setSelected(false);
            selectedActor.get().setPath(possiblePath);
            selectedActor = Optional.empty();
            possiblePath = new ArrayList<>();
        }

        if (selectedActor.isPresent() && hoveredOnOtherActor) {
            possiblePath = new ArrayList<>();
        }
    }

    private void updatePossiblePath() {
        int cursorX = cursorCamera.getCursorX();
        int cursorY = cursorCamera.getCursorY();
        Optional<Actor> hoveredActor = actors.stream().filter(
                actor -> actor.getX() == cursorX && actor.getY() == cursorY).findFirst();
        boolean hoveredOnEmptyTile = hoveredActor.isEmpty();

        if (selectedActor.isPresent() && hoveredOnEmptyTile) {
            // do pathfinding
            Pathfinder pathfinder = new Pathfinder(tileMap, actors);
            int actorX = (int) selectedActor.get().getX();
            int actorY = (int) selectedActor.get().getY();
            System.out.println("Recalculating path from " + actorX + ", " + actorY + " to " + cursorX + ", " + cursorY);
            Tile start = tileMap.getTile(actorX, actorY);
            Tile end = tileMap.getTile(cursorX, cursorY);
            possiblePath = pathfinder.find(start, end);
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.DARK_GRAY.darker().darker().darker());
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        starBackground.onRender(graphics);

        AffineTransform restore = graphics.getTransform();
        AffineTransform transform = camera.getTransform();
        graphics.setTransform(transform);

        // Draw the map
        tileMap.onRender(graphics);

        // Draw the actors
        for (Actor actor : actors) {
            actor.onRender(graphics);
        }


        if (!possiblePath.isEmpty()) {
            int tileSize = getGame().TILE_SIZE;
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

        // Draw the tileCursor
        cursorCamera.onRender(graphics);

        graphics.setTransform(restore);


        if (actionMenuOpen) {
            UserInterface ui = new UserInterface(graphics, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT,
                                                 getGame().TILE_SIZE
            );

            int menuWidth = 5 * ui.tileSize;
            int menuHeight = 7 * ui.tileSize;
            int menuX = ui.screenWidth - menuWidth - ui.tileSize;
            int menuY = (ui.screenHeight / 2) - (menuHeight / 2);

            ui.textSize = 16;
            ui.textColor = Color.WHITE;

            ui.drawPanel(menuX, menuY, menuWidth, menuHeight);
        }
    }
}

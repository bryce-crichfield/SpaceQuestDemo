package game.state.battle.model;

import game.util.Util;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class World {
    private final int width;
    private final int height;
    private final Tile[][] tiles;
    private final List<Actor> actors = new ArrayList<>();

    public World(int width, int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[width][height];

        // Initialize the tiles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = Color.cyan;
                tiles[x][y] = new ColorTile(x, y, true, color);
            }
        }

        actors.add(new Actor(3, 0, Color.RED));
        actors.add(new Actor(5, 0, Color.BLUE));
        actors.add(new Actor(6, 0, Color.MAGENTA));
    }

    public void wall(int x, int y) {
        tiles[x][y] = new ColorTile(x, y, false, Color.RED.darker().darker().darker());
    }

    public void onUpdate(Duration duration) {
        for (Actor actor : actors) {
            actor.onUpdate(duration);
        }
    }

    public void onRender(Graphics2D graphics) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].onRender(graphics);
            }
        }

        for (Actor actor : actors) {
            actor.onRender(graphics);
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[] getNeighbors(int x, int y) {
        List<Tile> neighbors = new ArrayList<>();

        // Up
        if (y > 0) {
            neighbors.add(tiles[x][y - 1]);
        }

        // Down
        if (y < height - 1) {
            neighbors.add(tiles[x][y + 1]);
        }

        // Left
        if (x > 0) {
            neighbors.add(tiles[x - 1][y]);
        }

        // Right
        if (x < width - 1) {
            neighbors.add(tiles[x + 1][y]);
        }

        return neighbors.toArray(new Tile[0]);
    }

    public Optional<Actor> getActorByPosition(int x, int y) {
        for (Actor actor : actors) {
            if (actor.getX() == x && actor.getY() == y) {
                return Optional.of(actor);
            }
        }

        return Optional.empty();
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
    }

    public List<Actor> getActors() {
        return actors;
    }

    public Optional<Actor> findActor(Predicate<Actor> predicate) {
        for (Actor actor : actors) {
            if (predicate.test(actor)) {
                return Optional.of(actor);
            }
        }

        return Optional.empty();
    }

    public Raycast raycast(int startX, int startY, int endX, int endY) {
        return new Raycast(tiles, startX, startY, endX, endY);
    }

    public List<Tile> getTilesInRange(int x, int y, int walkDistance) {
        List<Tile> found = new ArrayList<>();

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (Util.distance(x, y, tile.getX(), tile.getY()) <= walkDistance) {
                    found.add(tile);
                }
            }
        }

        return found;
    }
}

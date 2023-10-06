package game.battle;

import game.Util;

import java.util.*;

public class Pathfinder {
    TileMap map;
    List<Actor> actors;
    Queue<Node> open;
    Set<Node> closed;

    public Pathfinder(TileMap map, List<Actor> actors) {
        this.map = map;
        this.actors = actors;
        open = new PriorityQueue<>(Comparator.comparingInt(n -> n.gScore + n.hScore));
        closed = new HashSet<>();
    }

    public List<Tile> find(Tile start, Tile end) {
        if (!start.isPassable() || !end.isPassable()) {
            return List.of();
        }

        open.clear();
        closed.clear();

        int hScore = (int) Util.distance(start.getX(), start.getY(), end.getX(), end.getY());
        int gScore = 0;
        Node startNode = new Node(start.getX(), start.getY(), gScore, hScore, null);
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();
            if (current.x == end.getX() && current.y == end.getY()) {
                return reconstruct(current);
            } else {
                expand(current, end);
                closed.add(current);
            }
        }

        return List.of();
    }

    private List<Tile> reconstruct(Node end) {
        List<Tile> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(map.getTile(current.x, current.y));
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public void expand(Node current, Tile end) {
        // Add the neighbors to the open list
        for (Tile neighbor : map.getNeighbors(current.x, current.y)) {
            if (!neighbor.isPassable() || actorOccupies(neighbor.getX(), neighbor.getY())) {
                continue;
            }

            int gScore = (int) Util.distance(current.x, current.y, neighbor.getX(), neighbor.getY());
            int hScore = (int) Util.distance(neighbor.getX(), neighbor.getY(), end.getX(), end.getY());

            Node neighborNode = new Node(neighbor.getX(), neighbor.getY(), gScore, hScore, current);
            if (open.contains(neighborNode)) {
                continue;
            }

            if (!closed.contains(neighborNode)) {
                open.add(neighborNode);
            }
        }
    }

    public boolean actorOccupies(int x, int y) {
        for (Actor actor : actors) {
            if (actor.getX() == x && actor.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private static class Node {
        int x;
        int y;
        int gScore;
        int hScore;
        Node parent;

        public Node(int x, int y, int gScore, int hScore, Node parent) {
            this.x = x;
            this.y = y;
            this.gScore = gScore;
            this.hScore = hScore;
            this.parent = parent;
        }
    }
}

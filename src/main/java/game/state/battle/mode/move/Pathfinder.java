package game.state.battle.mode.move;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorMoved;
import game.state.battle.event.CursorMoved;
import game.state.battle.world.Actor;
import game.state.battle.world.Tile;
import game.state.battle.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pathfinder {
    private final BattleState battleState;
    private final World world;
    private final Actor actor;
    int cursorX = 0;
    int cursorY = 0;

    private List<Tile> possiblePath;

    public Pathfinder(BattleState state, World world, Actor actor) {
        this.battleState = state;
        this.world = world;
        this.actor = actor;
        possiblePath = new ArrayList<>();
    }

    public void onCursorMoved(CursorMoved event) {
        cursorX = event.cursor.getCursorX();
        cursorY = event.cursor.getCursorY();

        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();
        if (!hoveringOnEmptyTile) {
            possiblePath = new ArrayList<>();
            return;
        }

        PathfindingStrategy pathfindingStrategy = new PathfindingStrategy(world, actor);
        Tile start = world.getTile((int) actor.getX(), (int) actor.getY());
        Tile end = world.getTile(cursorX, cursorY);
        possiblePath = pathfindingStrategy.find(start, end);
    }

    public void onKeyPressed(Integer keyCode) {
        boolean primaryPressed = keyCode == Keyboard.PRIMARY;
        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();

        if (hoveringOnEmptyTile && primaryPressed) {
            // TODO: This should really be a move command
            battleState.getGame().getAudio().play("select.wav");
            if (possiblePath.isEmpty()) {
                return;
            }
            ActorMoved.event.fire(new ActorMoved(actor, possiblePath));
            possiblePath = new ArrayList<>();
        }
    }

    public void onRender(Graphics2D graphics) {
        List<Tile> inRange = world.getTilesInRange((int) actor.getX(), (int) actor.getY(), actor.getWalkDistance());

        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for (Tile tile : inRange) {
            graphics.setColor(Color.ORANGE.darker().darker());
            graphics.fillRect(tile.getX() * 32, tile.getY() * 32, 32, 32);
        }
        graphics.setComposite(originalComposite);

        Tile.drawOutline(inRange, graphics, Color.ORANGE);


        Tile.drawTurtle(possiblePath, graphics, Color.ORANGE);

    }
}

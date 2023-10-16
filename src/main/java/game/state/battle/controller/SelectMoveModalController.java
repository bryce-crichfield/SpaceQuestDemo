package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.*;
import game.state.battle.model.world.Tile;
import game.state.battle.model.actor.Actor;
import game.state.battle.model.world.World;
import game.state.battle.model.world.Pathfinder;
import game.state.battle.util.Cursor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelectMoveModalController extends ModalController {
    private final World world;
    private final Actor actor;
    private int cursorX = 0;
    private int cursorY = 0;
    private List<Tile> possiblePath;

    public SelectMoveModalController(BattleState battleState) {
        super(battleState);

        this.world = battleState.getWorld();
        possiblePath = new ArrayList<>();

        Optional<Actor> actor = battleState.getSelector().getCurrentlySelectedActor();
        if (actor.isEmpty())
            throw new IllegalStateException("Attempting to enter SelectMoveController without a selected actor");
        this.actor = actor.get();
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.ORANGE);


        on(getBattleState().getOnWorldRender()).run(this::onRender);

        on(CursorMoved.event).run(this::onCursorMoved);
        on(CursorMoved.event).run(getBattleState().getSelector()::onCursorMoved);

        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getSelector()::onKeyPressed);
        // Order matters here.  We want the cursor to update before we check if it's on an empty tile in the keyPressed event.
        on(Keyboard.keyPressed).run(this::onKeyPressed);

        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ControllerTransition.defer.fire(SelectActionModalController::new);
            }
        });

        on(ActionActorMoved.event).run(event -> ControllerTransition.defer.fire(SelectActionModalController::new));
        on(ActorSelectionSwap.event).run(event -> ControllerTransition.defer.fire(SelectActionModalController::new));
    }

    private void onCursorMoved(Cursor cursor) {
        cursorX = cursor.getCursorX();
        cursorY = cursor.getCursorY();

        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();
        if (!hoveringOnEmptyTile) {
            possiblePath = new ArrayList<>();
            return;
        }

        Pathfinder pathfinder = new Pathfinder(world, actor);
        Tile start = world.getTile((int) actor.getX(), (int) actor.getY());
        Tile end = world.getTile(cursorX, cursorY);
        possiblePath = pathfinder.find(start, end);
    }

    private void onKeyPressed(Integer keyCode) {
        boolean primaryPressed = keyCode == Keyboard.PRIMARY;
        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();

        if (hoveringOnEmptyTile && primaryPressed) {
            // TODO: This should really be a move command
            getBattleState().getGame().getAudio().play("select.wav");
            if (possiblePath.isEmpty()) {
                return;
            }
            ActionActorMoved.event.fire(new ActionActorMoved(actor, possiblePath));
            possiblePath = new ArrayList<>();
        }
    }

    public void onRender(Graphics2D graphics) {
        int distance = actor.getMovementPoints();
        List<Tile> inRange = world.getTilesInRange((int) actor.getX(), (int) actor.getY(), distance);

        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for (Tile tile : inRange) {
            graphics.setColor(Color.ORANGE.darker().darker());
            graphics.fillRect(tile.getX() * 32, tile.getY() * 32, 32, 32);
        }
        graphics.setComposite(originalComposite);

        Tile.drawOutline(inRange, graphics, Color.ORANGE);


        Tile.drawTurtle(possiblePath, graphics, Color.ORANGE);

        getBattleState().getCursor().onRender(graphics);
    }
}

package game.state.battle.player;

import game.io.Keyboard;
import game.state.battle.event.*;
import game.state.battle.hud.HudStats;
import game.state.battle.model.Tile;
import game.state.battle.model.Actor;
import game.state.battle.model.World;
import game.state.battle.model.Pathfinder;
import game.event.Event;
import game.util.Util;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelectMovePlayerMode extends PlayerMode {
    private final HudStats selectedActorStats;
    private final HudStats hoveredActorStats;
    Event<Actor> onChangeHovered = new Event<>();
    private List<Tile> possiblePath;

    public SelectMovePlayerMode(PlayerMode controller) {
        this(controller.world, controller.cursor, controller.actor.get());
    }

    public SelectMovePlayerMode(World world, Cursor cursor, Actor selected) {
        super(world, cursor);

        this.actor = Optional.of(selected);

        possiblePath = new ArrayList<>();

        Event<Actor> onChangeSelected = new Event<>();
        selectedActorStats = new HudStats(20, 20, onChangeSelected);
        selectedActorStats.setVisible(true);
        onChangeSelected.fire(selected);

        hoveredActorStats = new HudStats(280, 20, onChangeHovered);
        hoveredActorStats.setVisible(false);
        onChangeHovered.fire(selected);
    }

    @Override
    public void onKeyReleased(int keyCode) {

    }

    @Override
    public void onEnter() {
        cursor.enterBlinkingMode();
        cursor.setColor(Color.ORANGE);

        on(CursorMoved.event).run(this::onCursorMoved);
        on(ActionActorMoved.event).run(this::actorMoved);
    }

    public void actorMoved(ActionActorMoved movement) {
        Util.ensure(actor.isPresent(), "No actor selected in the select move mode");

        // the actor has now moved and can no longer move this turn, it is waiting for the next turn
        if (actor.get().getMovementPoints() <= 0) {
            actor.get().setWaiting(true);
            deselectActor();
            ControllerTransition.defer.fire(() -> new ObserverPlayerMode(world, cursor));
        } else {
            ControllerTransition.defer.fire(() -> new SelectActionPlayerMode(this));
        }
    }

    private void onCursorMoved(Cursor cursor) {
        int cursorX = cursor.getCursorX();
        int cursorY = cursor.getCursorY();

        Optional<Actor> hoveredActor = world.getActorByPosition(cursorX, cursorY);
        boolean hoveringOnEmptyTile = hoveredActor.isEmpty();
        if (hoveringOnEmptyTile) {
            Pathfinder pathfinder = new Pathfinder(world, actor.get());
            Tile start = world.getTile((int) actor.get().getX(), (int) actor.get().getY());
            Tile end = world.getTile(cursorX, cursorY);
            possiblePath = pathfinder.find(start, end);

            hoveredActorStats.setVisible(false);
        } else {
            possiblePath = new ArrayList<>();

            if (hoveredActor.get().equals(actor.get())) {
                hoveredActorStats.setVisible(false);
                return;
            }

            Actor hovered = hoveredActor.get();
            hoveredActorStats.setVisible(true);
            onChangeHovered.fire(hovered);
        }
    }

    @Override
    public void onUpdate(Duration delta) {
        cursor.onUpdate(delta);
    }

    @Override
    public void onGuiRender(Graphics2D graphics) {
        selectedActorStats.onRender(graphics);
        hoveredActorStats.onRender(graphics);
    }

    @Override
    public void onWorldRender(Graphics2D graphics) {
        int distance = actor.get().getMovementPoints();
        List<Tile> inRange = world.getTilesInRange((int) actor.get().getX(), (int) actor.get().getY(), distance);

        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for (Tile tile : inRange) {
            graphics.setColor(Color.ORANGE.darker().darker());
            graphics.fillRect(tile.getX() * 32, tile.getY() * 32, 32, 32);
        }
        graphics.setComposite(originalComposite);

        Tile.drawOutline(inRange, graphics, Color.ORANGE);


        Tile.drawTurtle(possiblePath, graphics, Color.ORANGE);

        cursor.onRender(graphics);
    }

    @Override
    public void onExit() {
        unsubscribeAll();
    }

    @Override
    public void onKeyPressed(int keyCode) {
        cursor.onKeyPressed(keyCode);

        boolean primaryPressed = keyCode == Keyboard.PRIMARY;
        int cursorX = cursor.getCursorX();
        int cursorY = cursor.getCursorY();
        boolean hoveringOnEmptyTile = world.getActorByPosition(cursorX, cursorY).isEmpty();

        if (hoveringOnEmptyTile && primaryPressed) {
//            getBattleState().getGame().getAudio().play("select.wav");
            if (possiblePath.isEmpty()) {
                return;
            }

            ActionActorMoved.event.fire(new ActionActorMoved(actor.get(), possiblePath));
            possiblePath = new ArrayList<>();
        }

        if (keyCode == Keyboard.SECONDARY) {
            if (actor.isEmpty()) {
                throw new IllegalStateException("No actor selected in the select action mode");
            }
            ControllerTransition.defer.fire(() -> new SelectActionPlayerMode(this));
        }
    }

}

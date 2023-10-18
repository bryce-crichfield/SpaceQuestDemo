package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.*;
import game.state.battle.hud.HudStats;
import game.state.battle.model.world.Tile;
import game.state.battle.model.actor.Actor;
import game.state.battle.model.world.World;
import game.state.battle.model.world.Pathfinder;
import game.state.battle.util.Cursor;
import game.event.Event;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelectMovePlayerController extends PlayerController {
    private final World world;
    private final Actor selectedActor;
    private int cursorX = 0;
    private int cursorY = 0;
    private List<Tile> possiblePath;
    private HudStats selectedActorStats;
    private HudStats hoveredActorStats;
    Event<Actor> onChangeHovered = new Event<>();


    public SelectMovePlayerController(BattleState battleState, Actor selected) {
        super(battleState);

        this.world = battleState.getWorld();
        possiblePath = new ArrayList<>();

        this.selectedActor = selected;

        Event<Actor> onChangeSelected = new Event<>();
        selectedActorStats = new HudStats(20, 20, onChangeSelected);
        selectedActorStats.setVisible(true);
        onChangeSelected.fire(selectedActor);

        hoveredActorStats = new HudStats(280, 20, onChangeHovered);
        hoveredActorStats.setVisible(false);
        onChangeHovered.fire(selectedActor);
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.ORANGE);



        on(CursorMoved.event).run(this::onCursorMoved);
        on(CursorMoved.event).run(getBattleState().getSelector()::onCursorMoved);

        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getSelector()::onKeyPressed);
        // Order matters here.  We want the cursor to update before we check if it's on an empty tile in the keyPressed event.
        on(Keyboard.keyPressed).run(this::onKeyPressed);

        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                var actor = getBattleState().getSelector().getCurrentlySelectedActor();
                if (actor.isEmpty()) {
                    throw new IllegalStateException("No actor selected in the select action mode");
                }
                ControllerTransition.defer.fire(state -> new SelectActionPlayerController(state, actor.get()));
            }
        });

        on(ActionActorMoved.event).run(movement -> {
            // the actor has now moved and can no longer move this turn, it is waiting for the next turn
            if (selectedActor.getMovementPoints() <= 0) {
                selectedActor.setWaiting(true);
                ActorUnselected.event.fire(selectedActor);
                getBattleState().getSelector().deselectActor();
                ControllerTransition.defer.fire(ObserverPlayerController::new);
            } else {
                ControllerTransition.defer.fire(state -> new SelectActionPlayerController(state, selectedActor));
            }

        });

        on(ActorSelectionSwap.event).run(actor -> {
            if (actor.equals(selectedActor)) {
                return;
            } else if (actor.isWaiting()) {
                return;
            }
//            ControllerTransition.defer.fire(state -> new SelectActionPlayerController(state, actor));
        });

        on(getBattleState().getOnWorldRender()).run(this::onRender);
        on(getBattleState().getOnGuiRender()).run(selectedActorStats::onRender);
        on(getBattleState().getOnGuiRender()).run(hoveredActorStats::onRender);
    }

    private void onCursorMoved(Cursor cursor) {
        cursorX = cursor.getCursorX();
        cursorY = cursor.getCursorY();

        Optional<Actor> hoveredActor = world.getActorByPosition(cursorX, cursorY);
        boolean hoveringOnEmptyTile = hoveredActor.isEmpty();
        if (hoveringOnEmptyTile) {
            Pathfinder pathfinder = new Pathfinder(world, selectedActor);
            Tile start = world.getTile((int) selectedActor.getX(), (int) selectedActor.getY());
            Tile end = world.getTile(cursorX, cursorY);
            possiblePath = pathfinder.find(start, end);

            hoveredActorStats.setVisible(false);
        } else {
            possiblePath = new ArrayList<>();

            if (hoveredActor.get().equals(selectedActor)) {
                hoveredActorStats.setVisible(false);
                return;
            }

            Actor hovered = hoveredActor.get();
            hoveredActorStats.setVisible(true);
            onChangeHovered.fire(hovered);
        }
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

            ActionActorMoved.event.fire(new ActionActorMoved(selectedActor, possiblePath));
            possiblePath = new ArrayList<>();
        }
    }

    public void onRender(Graphics2D graphics) {
        int distance = selectedActor.getMovementPoints();
        List<Tile> inRange = world.getTilesInRange((int) selectedActor.getX(), (int) selectedActor.getY(), distance);

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

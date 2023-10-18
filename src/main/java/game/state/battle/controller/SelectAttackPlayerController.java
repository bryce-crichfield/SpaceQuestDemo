package game.state.battle.controller;

import game.event.Event;
import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActionActorAttack;
import game.state.battle.event.ActorUnselected;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.HudStats;
import game.state.battle.model.actor.Actor;
import game.state.battle.model.world.Raycast;
import game.state.battle.model.world.Tile;
import game.state.battle.util.Cursor;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SelectAttackPlayerController extends PlayerController {
    Raycast raycast;
    Actor selectedActor;
    private final game.event.Event<Actor> onChangeHovered;
    private final HudStats hoveredActorStats;

    public SelectAttackPlayerController(BattleState battleState, Actor selectedActor) {
        super(battleState);
        this.selectedActor = selectedActor;

        onChangeHovered = new Event<>();
        hoveredActorStats = new HudStats(20, 20, onChangeHovered);
        hoveredActorStats.setVisible(false);
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.RED);

        on(CursorMoved.event).run(this::onCursorMoved);

        on(Keyboard.keyPressed).run(this::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);

        on(getBattleState().getOnWorldRender()).run(this::onRender);
        on(getBattleState().getOnGuiRender()).run(hoveredActorStats::onRender);
    }

    public void onKeyPressed(Integer keyCode) {
        if (keyCode == Keyboard.PRIMARY) {
            getBattleState().getGame().getAudio().play("select.wav");
            getBattleState().getCursor().setPosition((int) selectedActor.getX(), (int) selectedActor.getY());
            ActionActorAttack.event.fire(new ActionActorAttack(selectedActor, raycast.getTiles()));

            // The actor has issued its attack, it is now waiting.
            selectedActor.setWaiting(true);
            ActorUnselected.event.fire(selectedActor);
            getBattleState().getSelector().deselectActor();
            ControllerTransition.defer.fire(ObserverPlayerController::new);
        }

        if (keyCode == Keyboard.SECONDARY) {
            var actor = getBattleState().getSelector().getCurrentlySelectedActor();
            if (actor.isEmpty()) {
                throw new IllegalStateException("No actor selected in the select action mode");
            }
            ControllerTransition.defer.fire(state -> new SelectActionPlayerController(state, actor.get()));
        }
    }

    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);

        if (raycast == null) {
            return;
        }
        List<Tile> possiblePath = raycast.getTiles();

        Tile.drawOutline(possiblePath, graphics, Color.RED);

        List<Tile> inRange = getBattleState().getWorld().getTilesInRange((int) selectedActor.getX(), (int) selectedActor.getY(),
                                                                         selectedActor.getAttackDistance()
        );

        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for (Tile tile : inRange) {
            graphics.setColor(Color.RED.darker().darker());
            graphics.fillRect(tile.getX() * 32, tile.getY() * 32, 32, 32);
        }
        graphics.setComposite(originalComposite);

        Tile.drawOutline(inRange, graphics, Color.RED);
    }

    private void onCursorMoved(Cursor cursor) {
        int cursorX = cursor.getCursorX();
        int cursorY = cursor.getCursorY();
        raycast = getBattleState().getWorld().raycast((int) selectedActor.getX(), (int) selectedActor.getY(), cursorX, cursorY);

        Optional<Actor> actor = getBattleState().getWorld().getActorByPosition(cursorX, cursorY);
        if (actor.isEmpty()) {
            hoveredActorStats.setVisible(false);
        }

        if (actor.isPresent()) {

            if (actor.get() == this.selectedActor) {
                hoveredActorStats.setVisible(false);
                return;
            }

            Actor hovered = actor.get();
            hoveredActorStats.setVisible(true);
            onChangeHovered.fire(hovered);
        }
    }
}

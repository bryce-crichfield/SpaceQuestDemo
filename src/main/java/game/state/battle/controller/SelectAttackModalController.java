package game.state.battle.controller;

import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActionActorAttack;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ControllerTransition;
import game.state.battle.model.Actor;
import game.state.battle.model.Raycast;
import game.state.battle.model.Tile;
import game.state.battle.util.Cursor;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SelectAttackModalController extends ModalController {
    Raycast raycast;
    Actor actor;

    public SelectAttackModalController(BattleState battleState) {
        super(battleState);

        Optional<Actor> selectedActor = getBattleState().getSelector().getCurrentlySelectedActor();
        if (selectedActor.isEmpty()) {
            throw new IllegalStateException("Attempting to enter SelectAttackController without a selected actor");
        }

        this.actor = selectedActor.get();
    }

    @Override
    public void onEnter() {
        getBattleState().getCursor().enterBlinkingMode();
        getBattleState().getCursor().setColor(Color.RED);

        on(CursorMoved.event).run(this::onCursorMoved);
        on(Keyboard.keyPressed).run(this::onKeyPressed);
        on(Keyboard.keyPressed).run(getBattleState().getCursor()::onKeyPressed);
        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ControllerTransition.defer.fire(SelectActionModalController::new);
            }
        });

        on(getBattleState().getOnWorldRender()).run(this::onRender);

    }

    public void onKeyPressed(Integer keyCode) {
        if (keyCode == Keyboard.PRIMARY) {
            getBattleState().getGame().getAudio().play("select.wav");
            getBattleState().getCursor().setPosition((int) actor.getX(), (int) actor.getY());
            ActionActorAttack.event.fire(new ActionActorAttack(actor, raycast.getTiles()));
            ControllerTransition.defer.fire(SelectActionModalController::new);
        }
    }

    public void onRender(Graphics2D graphics) {
        getBattleState().getCursor().onRender(graphics);

        if (raycast == null) {
            return;
        }
        List<Tile> possiblePath = raycast.getTiles();

        Tile.drawOutline(possiblePath, graphics, Color.RED);

        List<Tile> inRange = getBattleState().getWorld().getTilesInRange((int) actor.getX(), (int) actor.getY(),
                                                                         actor.getAttackDistance()
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
        raycast = getBattleState().getWorld().raycast((int) actor.getX(), (int) actor.getY(), cursorX, cursorY);
    }
}

package game.state.battle.player;

import game.event.Event;
import game.form.properties.FormAlignment;
import game.form.properties.layout.FormVerticalLayout;
import game.io.Keyboard;
import game.state.battle.event.ActorHovered;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.HudActions;
import game.state.battle.hud.HudStats;
import game.state.battle.model.Actor;

import java.awt.*;
import java.time.Duration;

public class SelectActionPlayerMode extends PlayerMode {
    private final HudStats hudStats;
    private final HudActions hudActions;

    public SelectActionPlayerMode(PlayerMode controller) {
        super(controller.world, controller.cursor, controller.actor.get());

        Event<Actor> onChange = new Event<>();
        hudStats = new HudStats(20, 20, onChange);
        hudStats.setVisible(true);
        onChange.fire(actor.get());

        hudActions = new HudActions(20, 210, this);
        hudActions.setVisible(true);
        hudActions.setLayout(new FormVerticalLayout(FormAlignment.CENTER));
    }

    @Override
    public final void onKeyPressed(int keycode) {
        hudActions.onKeyPressed(keycode);

        if (keycode == Keyboard.SECONDARY) {
            if (actor.isEmpty()) {
                throw new IllegalStateException("No actor selected in the select action mode");
            }

            deselectActor();

            ControllerTransition.defer.fire(() -> {
                int cursorX = cursor.getCursorX();
                int cursorY = cursor.getCursorY();
                if (cursorX == actor.get().getX() && cursorY == actor.get().getY()) {
                    ActorHovered.event.fire(actor.get());
                }
                return new ObserverPlayerMode(this);
            });
        }
    }

    @Override
    public void onKeyReleased(int keyCode) {

    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {
    }

    @Override
    public void onGuiRender(Graphics2D graphics) {
        hudStats.onRender(graphics);
        hudActions.onRender(graphics);
    }

    @Override
    public void onWorldRender(Graphics2D graphics) {

    }

    @Override
    public void onExit() {
        unsubscribeAll();
    }
}

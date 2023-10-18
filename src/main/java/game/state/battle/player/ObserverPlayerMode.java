package game.state.battle.player;

import game.io.Keyboard;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.HudStats;
import game.state.battle.model.Actor;
import game.event.Event;
import game.state.battle.model.World;

import java.awt.*;
import java.time.Duration;
import java.util.Optional;

public class ObserverPlayerMode extends PlayerMode {
    private final Event<Actor> onChangeHovered;
    private final HudStats hoveredActorStats;


    public ObserverPlayerMode(World world, Cursor cursor) {
        super(world, cursor);
        onChangeHovered = new Event<>();
        hoveredActorStats = new HudStats(20, 20, onChangeHovered);
        hoveredActorStats.setVisible(false);
        hoverActor(cursor);
    }

    public ObserverPlayerMode(PlayerMode controller) {
        this(controller.world, controller.cursor);
    }

    @Override
    public void onKeyPressed(int keyCode) {
        cursor.onKeyPressed(keyCode);
        if (keyCode == Keyboard.PRIMARY) {
            this.selectActor();
        }
    }

    @Override
    public void onKeyReleased(int keyCode) {

    }

    private void forceHoveredActorStats() {
        int cx = cursor.getCursorX();
        int cy = cursor.getCursorY();
        Optional<Actor> hov = world.getActorByPosition(cx, cy);
        hov.ifPresent(actor -> {
            onChangeHovered.fire(actor);
            hoveredActorStats.setVisible(true);
        });
    }

    @Override
    public void onEnter() {
        forceHoveredActorStats();

        cursor.enterBlinkingMode();
        cursor.setColor(Color.WHITE);

        on(ActorSelected.event).run(actor -> {
            ControllerTransition.defer.fire(() -> new SelectActionPlayerMode(this));
        });

        on(CursorMoved.event).run(this::hoverActor);
    }

    @Override
    public void onUpdate(Duration delta) {
        cursor.onUpdate(delta);
    }

    @Override
    public void onGuiRender(Graphics2D graphics) {
        hoveredActorStats.onRender(graphics);
    }

    @Override
    public void onWorldRender(Graphics2D graphics) {
        cursor.onRender(graphics);
    }

    @Override
    public void onExit() {
        unsubscribeAll();
    }

    public void onRender(Graphics2D graphics) {
        cursor.onRender(graphics);
    }

    private final void hoverActor(Cursor cursor) {
        int cursorX = cursor.getCursorX();
        int cursorY = cursor.getCursorY();

        Optional<Actor> actor = world.getActorByPosition(cursorX, cursorY);
        if (actor.isEmpty()) {
            hoveredActorStats.setVisible(false);
        }

        if (actor.isPresent()) {
            Actor hovered = actor.get();
            hoveredActorStats.setVisible(true);
            onChangeHovered.fire(hovered);
        }
    }
 }

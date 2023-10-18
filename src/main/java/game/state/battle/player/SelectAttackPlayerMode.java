package game.state.battle.player;

import game.event.Event;
import game.io.Keyboard;
import game.state.battle.event.ActionActorAttack;
import game.state.battle.event.CursorMoved;
import game.state.battle.event.ControllerTransition;
import game.state.battle.hud.HudStats;
import game.state.battle.model.Actor;
import game.state.battle.model.Raycast;
import game.state.battle.model.Tile;
import game.state.battle.model.World;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class SelectAttackPlayerMode extends PlayerMode {
    private final game.event.Event<Actor> onChangeHovered;
    private final HudStats hoveredActorStats;
    Raycast raycast;

    public SelectAttackPlayerMode(PlayerMode controller) {
        this(controller.world, controller.cursor, controller.actor.get());
    }

    public SelectAttackPlayerMode(World world, Cursor cursor, Actor actor) {
        super(world, cursor, actor);

        onChangeHovered = new Event<>();
        hoveredActorStats = new HudStats(20, 20, onChangeHovered);
        hoveredActorStats.setVisible(false);
    }

    @Override
    public void onKeyPressed(int keyCode) {
        cursor.onKeyPressed(keyCode);


        if (keyCode == Keyboard.PRIMARY) {
//            getBattleState().getGame().getAudio().play("select.wav");
            cursor.setPosition((int) actor.get().getX(), (int) actor.get().getY());
            ActionActorAttack.event.fire(new ActionActorAttack(actor.get(), raycast.getTiles()));

            // The actor has issued its attack, it is now waiting.
            actor.get().setWaiting(true);
            deselectActor();
            ControllerTransition.defer.fire(() -> new ObserverPlayerMode(world, cursor));
        }

        if (keyCode == Keyboard.SECONDARY) {
            if (actor.isEmpty()) {
                throw new IllegalStateException("No actor selected in the select action mode");
            }
            ControllerTransition.defer.fire(() -> new SelectActionPlayerMode(this));
        }
    }

    @Override
    public void onKeyReleased(int keyCode) {

    }

    @Override
    public void onEnter() {
        cursor.enterBlinkingMode();
        cursor.setColor(Color.RED);

        on(CursorMoved.event).run(this::onCursorMoved);
    }

    private void onCursorMoved(Cursor cursor) {
        int cursorX = cursor.getCursorX();
        int cursorY = cursor.getCursorY();
        raycast = world.raycast((int) actor.get().getX(), (int) actor.get().getY(), cursorX, cursorY);

        Optional<Actor> hoveredActor = world.getActorByPosition(cursorX, cursorY);
        if (hoveredActor.isEmpty()) {
            hoveredActorStats.setVisible(false);
        }

        if (hoveredActor.isPresent()) {
            if (hoveredActor.get() == this.actor.get()) {
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
        hoveredActorStats.onRender(graphics);
    }

    @Override
    public void onWorldRender(Graphics2D graphics) {
        cursor.onRender(graphics);

        if (raycast == null) {
            return;
        }
        List<Tile> possiblePath = raycast.getTiles();

        Tile.drawOutline(possiblePath, graphics, Color.RED);

        List<Tile> inRange = world.getTilesInRange((int) actor.get().getX(), (int) actor.get().getY(),
                                                   actor.get().getAttackDistance()
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

    @Override
    public void onExit() {

    }
}

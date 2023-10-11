package game.state.battle;

import game.Game;
import game.event.Event;
import game.event.SubscriptionManager;
import game.form.element.FormElement;
import game.form.element.FormLabel;
import game.form.element.FormMenu;
import game.form.properties.FormAlignment;
import game.form.properties.FormBorder;
import game.form.properties.FormFill;
import game.form.properties.FormText;
import game.io.Keyboard;
import game.state.GameState;
import game.state.battle.event.*;
import game.state.battle.mode.ActionMode;
import game.state.battle.mode.ObserverMode;
import game.state.battle.util.Cursor;
import game.state.battle.util.Hoverer;
import game.state.battle.world.Actor;
import game.state.battle.world.World;
import game.state.title.StarBackground;
import game.util.Camera;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.Duration;
import java.util.Optional;

public class BattleState extends GameState {
    private final StarBackground starBackground;
    private final Camera camera;
    private final World world;
    private final Cursor cursor;
    private final Hoverer hoverer;
    private ActionMode mode;
    private final FormElement hoveredStats;

    public BattleState(Game game) {
        super(game);
        camera = new Camera(game);
        world = new World(16, 16);
        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        cursor = new Cursor(camera, game, world);
        hoverer = new Hoverer(world);

        getSubscriptions().on(ModeChanged.event).run(this::onActionModeEvent);
        ModeChanged.event.fire(new ObserverMode(this));
        ModeChanged.event.flush();

        for (Actor actor : world.getActors()) {
            getSubscriptions().on(ActorMoved.event).run(actor::onActorMoved);
            getSubscriptions().on(ActorSelected.event).run(actor::onActorSelected);
            getSubscriptions().on(ActorDeselected.event).run(actor::onActorDeselected);
            getSubscriptions().on(ActorAttacked.event).run(actor::onActorAttacked);
            getSubscriptions().on(ActorKilled.event).run(killed -> {
                world.removeActor(killed.getDead());
            });
        }

        getSubscriptions().on(CursorMoved.event).run(hoverer::onCursorMoved);


        hoveredStats = new FormElement(5, 5, 25, 60);
        FormFill fill = new FormFill();
        fill.setPaint(new Color(0x0A001A));
        fill.setRoundness(25);

        FormBorder border = new FormBorder();
        border.setInlayColor(Color.WHITE);
        border.setOutlineColor(Color.BLACK);
        border.setRounding(25);
        border.setThicknessInlay(3);
        border.setThicknessOutline(6);

        hoveredStats.setFill(fill);
        hoveredStats.setBorder(border);


        getSubscriptions().on(ActorHovered.event).run(hovered -> {
            hoveredStats.setVisible(true);
        });

        getSubscriptions().on(ActorUnhovered.event).run(unhovered -> {
            hoveredStats.setVisible(false);
        });

        FormLabel name = new FormLabel(100, 20);
        hoveredStats.addChild(name);

        getSubscriptions().on(this.getOnGuiRender()).run(hoveredStats::onRender);
    }

    private void onActionModeEvent(ActionMode newMode) {
        if (mode != null)
            mode.onExit();
        mode = newMode;
        mode.onEnter();
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);

        mode.onUpdate(delta);

        world.onUpdate(delta);

        if (getGame().getKeyboard().pressed(KeyEvent.VK_ESCAPE)) {
            getGame().popState();
        }

        ModeChanged.event.flush();
    }

    @Override
    public void onRender(Graphics2D graphics) {
        // Clear the screen
        graphics.setColor(new Color(0x0A001A));
        graphics.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        // Render the star background
        starBackground.onRender(graphics);

        // Get the camera transform and render the world
        AffineTransform restore = graphics.getTransform();
        AffineTransform transform = camera.getTransform();
        graphics.setTransform(transform);
        {
            world.onRender(graphics);
            getOnWorldRender().fire(graphics);
        }
        graphics.setTransform(restore);

        // Restore the transform and render the cursor camera
        getOnGuiRender().fire(graphics);
    }

    public Camera getCamera() {
        return camera;
    }

    public World getWorld() {
        return world;
    }

    public Cursor getCursor() {
        return cursor;
    }
}

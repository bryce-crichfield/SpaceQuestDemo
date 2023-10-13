package game.state.battle;

import game.Game;
import game.state.GameState;
import game.state.battle.event.*;
import game.state.battle.controller.ModalController;
import game.state.battle.controller.ObserverModalController;
import game.state.battle.hud.Hud;
import game.state.battle.util.Cursor;
import game.state.battle.util.Hoverer;
import game.state.battle.model.Actor;
import game.state.battle.model.World;
import game.state.battle.util.Selector;
import game.state.title.StarBackground;
import game.util.Camera;
import game.event.Event;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.Duration;

public class BattleState extends GameState {
    private final StarBackground starBackground;
    private final Camera camera;
    private final World world;
    private final Cursor cursor;
    private final Hoverer hoverer;
    private final Selector selector;
    private final Hud hud;
    private ModalController mode;

    public BattleState(Game game) {
        super(game);
        camera = new Camera(game);
        world = new World(16, 16);
        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        cursor = new Cursor(camera, game, world);
        hoverer = new Hoverer(world);
        selector = new Selector(world);
        Event<Actor> actorChanged = new Event<>();
        hud = new Hud(actorChanged);

        ControllerTransition.defer.fire(state -> new ObserverModalController(state));
        forceModeChange();

        for (Actor actor : world.getActors()) {
            getSubscriptions().on(ActionActorMoved.event).run(actor::onActorMoved);
            getSubscriptions().on(ActorSelected.event).run(actor::onActorSelected);
            getSubscriptions().on(ActorDeselected.event).run(actor::onActorDeselected);
            getSubscriptions().on(ActionActorAttack.event).run(actor::onActorAttacked);
            getSubscriptions().on(ActorKilled.event).run(world::removeActor);
        }

        getSubscriptions().on(CursorMoved.event).run(cursor -> {
            selector.onCursorMoved(cursor);
            hoverer.onCursorMoved(cursor);
        });

        getSubscriptions().on(ActorHovered.event).run(actor -> {
            if (selector.getCurrentlySelectedActor().isPresent())
                return;
            actorChanged.fire(actor);
            hud.getPrimary().setVisible(true);
        });

        getSubscriptions().on(ActorUnhovered.event).run(actor -> {
            if (selector.getCurrentlySelectedActor().isPresent())
                return;
            actorChanged.fire(actor);
            hud.getPrimary().setVisible(false);
        });

        getSubscriptions().on(ActorSelected.event).run(actor -> {
            actorChanged.fire(actor);
            hud.getPrimary().setVisible(true);
        });

        getSubscriptions().on(ActorDeselected.event).run(actor -> {
            actorChanged.fire(actor);
            hud.getPrimary().setVisible(false);
        });

        getSubscriptions().on(ActorAnimated.event).run(actorChanged::fire);

        getSubscriptions().on(getOnGuiRender()).run(graphics -> {
            hud.getPrimary().onRender(graphics);
            hud.getSecondary().onRender(graphics);
            hud.getActions().onRender(graphics);
        });
    }

    private void forceModeChange() {
        ControllerTransition.defer.flush(event -> {
            ModalController newMode = event.apply(this);
            if (mode != null)
                mode.onExit();
            mode = newMode;
            mode.onEnter();
        });
    }

    public Hud getHud() {
        return hud;
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);
        cursor.onUpdate(delta);

        world.onUpdate(delta);

        if (getGame().getKeyboard().pressed(KeyEvent.VK_ESCAPE)) {
            getGame().popState();
        }

        forceModeChange();
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

    public Selector getSelector() {
        return selector;
    }

    public Hoverer getHoverer() {
        return hoverer;
    }

    public Cursor getCursor() {
        return cursor;
    }
}

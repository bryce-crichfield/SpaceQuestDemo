package game.state.battle;

import game.Game;
import game.event.Event;
import game.event.SubscriptionManager;
import game.state.GameState;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ActorMoved;
import game.state.battle.event.ActorSelected;
import game.state.battle.event.ModeChanged;
import game.state.battle.mode.ActionMode;
import game.state.battle.mode.ObserverMode;
import game.state.battle.world.Actor;
import game.state.battle.world.World;
import game.state.title.StarBackground;
import game.util.Camera;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.Duration;

public class BattleState extends GameState {
    private final SubscriptionManager subscriptions = new SubscriptionManager();

    public static Event<Graphics2D> onWorldRender = new Event<>();
    public static Event<Graphics2D> onGuiRender = new Event<>();

    private final StarBackground starBackground;
    private final Camera camera;
    private final World world;
    private ActionMode mode;

    public BattleState(Game game) {
        super(game);
        camera = new Camera(game);
        world = new World(16, 16);
        starBackground = new StarBackground(this, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        subscriptions.on(ModeChanged.event).run(this::onActionModeEvent);
        ModeChanged.event.fire(new ObserverMode(this));
        ModeChanged.event.flush();

        for (Actor actor : world.getActors()) {
            subscriptions.on(ActorMoved.event).run(actor::onActorMoved);
            subscriptions.on(ActorSelected.event).run(actor::onActorSelected);
            subscriptions.on(ActorDeselected.event).run(actor::onActorDeselected);
        }
    }

    private void onActionModeEvent(ActionMode newMode) {
        if (mode != null)
            mode.onExit();
        mode = newMode;
        mode.onEnter();
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
    public void onExit() {
        onGuiRender.clear();
        onWorldRender.clear();
        subscriptions.unsubscribeAll();
    }

    @Override
    public void onRender(Graphics2D graphics) {
        // Clear the screen
        graphics.setColor(new Color(0x0A001A));
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        // Render the star background
        starBackground.onRender(graphics);

        // Get the camera transform and render the world
        AffineTransform restore = graphics.getTransform();
        AffineTransform transform = camera.getTransform();
        graphics.setTransform(transform);
        {
            world.onRender(graphics);
            onWorldRender.fire(graphics);
        }
        graphics.setTransform(restore);

        // Restore the transform and render the cursor camera
        onGuiRender.fire(graphics);
    }

    public Camera getCamera() {
        return camera;
    }

    public World getWorld() {
        return world;
    }
}

package game.state.battle;

import game.Game;
import game.io.Keyboard;
import game.state.GameState;
import game.state.battle.player.Cursor;
import game.state.battle.event.*;
import game.state.battle.player.Mode;
import game.state.battle.player.PlayerMode;
import game.state.battle.player.ObserverPlayerMode;
import game.state.battle.model.Actor;
import game.state.battle.model.World;
import game.state.title.StarBackground;
import game.util.Camera;
import game.util.Util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.Duration;
import java.util.Deque;
import java.util.LinkedList;

public class BattleState extends GameState {

    static class PlayerModeWrapper extends Mode {
        PlayerMode mode;

        public PlayerModeWrapper(World world, Cursor cursor) {
            super(world);
            this.mode = new ObserverPlayerMode(world, cursor);
            mode.onEnter();
        }

        @Override
        public void onKeyPressed(int keyCode) {
            mode.onKeyPressed(keyCode);
        }

        @Override
        public void onKeyReleased(int keyCode) {
            mode.onKeyReleased(keyCode);
        }

        @Override
        public void onEnter() {
            mode.onEnter();
        }

        @Override
        public void onUpdate(Duration delta) {
            mode.onUpdate(delta);

            ControllerTransition.defer.flush(event -> {
                var newMode = event.get();
                mode.onExit();
                mode = newMode;
                mode.onEnter();
            });
        }

        @Override
        public void onGuiRender(Graphics2D graphics) {
            mode.onGuiRender(graphics);
        }

        @Override
        public void onWorldRender(Graphics2D graphics) {
            mode.onWorldRender(graphics);
        }

        @Override
        public void onExit() {
            mode.onExit();
        }

        @Override
        public boolean isDone() {
            return mode.isDone();
        }
    }
    static class ComputerMode extends Mode {

        protected ComputerMode(World world) {
            super(world);
        }

        @Override
        public void onKeyPressed(int keyCode) {

        }

        @Override
        public void onKeyReleased(int keyCode) {

        }

        @Override
        public void onEnter() {

        }

        @Override
        public void onUpdate(Duration delta) {
            System.out.println("Computer mode");
        }

        @Override
        public void onGuiRender(Graphics2D graphics) {

        }

        @Override
        public void onWorldRender(Graphics2D graphics) {

        }

        @Override
        public void onExit() {

        }

        @Override
        public boolean isDone() {
            return false;
        }
    }
    private final StarBackground starBackground;
    private final Camera camera;
    private final World world;
    private final Deque<Mode> modes = new LinkedList<>();

    public BattleState(Game game) {
        super(game);
        camera = new Camera(game);
        world = new World(16, 16);
        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        for (Actor actor : world.getActors()) {
            getSubscriptions().on(ActionActorMoved.event).run(actor::onActorMoved);
            getSubscriptions().on(ActorSelected.event).run(actor::onActorSelected);
            getSubscriptions().on(ActorUnselected.event).run(actor::onActorDeselected);
            getSubscriptions().on(ActionActorAttack.event).run(actor::onActorAttacked);
            getSubscriptions().on(ActorKilled.event).run(world::removeActor);
            getSubscriptions().on(CursorMoved.event).run(actor::onCursorMoved);
        }

        var cursor = new Cursor(camera, game, world);
        var player = new PlayerModeWrapper(world, cursor);
        var computer = new ComputerMode(world);
        modes.addLast(player);
        modes.addLast(computer);
        modes.peek().onEnter();
    }

    @Override
    public void onEnter() {
        getSubscriptions().on(Keyboard.keyPressed).run(this::onKeyPressed);
    }

    public void onKeyPressed(int keycode) {
        if (!modes.isEmpty()) {
            modes.peek().onKeyPressed(keycode);
        }
    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);
        world.onUpdate(delta);

        if (getGame().getKeyboard().pressed(KeyEvent.VK_ESCAPE)) {
            getGame().popState();
        }

        if (!modes.isEmpty()) {
            modes.peek().onUpdate(delta);
            assert modes.peek() != null;
            if (modes.peek().isDone()) {
                var head = modes.pop();
                head.onExit();
                modes.addLast(head);
                assert !modes.isEmpty();
                modes.peek().onEnter();
            }
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        // Clear the screenw
        graphics.setColor(new Color(0x0A001A));
        graphics.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        // Render the star background
        starBackground.onRender(graphics);

        // Get the camera transform and render the world
        AffineTransform restore = graphics.getTransform();
        AffineTransform transform = camera.getTransform();
        graphics.setTransform(transform);
        world.onRender(graphics);
        if (!modes.isEmpty()) {
            modes.peek().onWorldRender(graphics);
        }

        graphics.setTransform(restore);

        // Restore the transform and render the cursor camera
        if (!modes.isEmpty()) {
            modes.peek().onGuiRender(graphics);
        }
    }
}

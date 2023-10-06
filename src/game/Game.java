package game;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Stack;

public class Game {
    public final int SCREEN_WIDTH = 480;
    public final int SCREEN_HEIGHT = 320;
    public final int TILE_SIZE = 32;
    private final Keyboard keyboard = new Keyboard();
    private final Audio audio = new Audio();
    private final Stack<GameState> stateStack = new Stack<>();

    public Audio getAudio() {
        return audio;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void pushState(GameState state) {
        if (!stateStack.isEmpty()) {
            stateStack.peek().onExit();
        }

        stateStack.push(state);
        state.onEnter();
    }

    public void popState() {
        if (!stateStack.isEmpty()) {
            stateStack.peek().onExit();
            stateStack.pop();
        }

        if (!stateStack.isEmpty()) {
            stateStack.peek().onEnter();
        }
    }

    void onUpdate(Duration delta) {
        if (!stateStack.isEmpty()) {
            stateStack.peek().onUpdate(delta);
        }

        keyboard.onUpdate();
    }

    void onRender(Graphics2D graphics) {
        Instant start = Instant.now();
        if (!stateStack.isEmpty()) {
            stateStack.peek().onRender(graphics);
        }
        Duration elapsed = Duration.between(start, Instant.now());
        float millis = elapsed.toNanos() / 1e6f;
//        System.out.println("Game.onRender() took " + millis + "ms");
    }
}

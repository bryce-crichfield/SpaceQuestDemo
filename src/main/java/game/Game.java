package game;

import game.event.LazyEvent;
import game.event.Event;
import game.io.Audio;
import game.io.Keyboard;
import game.state.GameState;


import java.awt.*;
import java.time.Duration;
import java.util.Stack;
import java.util.function.Consumer;

public class Game {
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 320;
    public static final int TILE_SIZE = 32;
    private final LazyEvent<Consumer<Game>> endOfUpdate = new LazyEvent<>();
    private final Keyboard keyboard = new Keyboard();
    private final Audio audio = new Audio();
    private final Stack<GameState> stateStack = new Stack<>();
    Game() throws Exception {
        // set volume
        // set volume
        audio.load(
                "resources/Shapeforms Audio Free Sound Effects/Dystopia – Ambience and Drone Preview/AUDIO/AMBIENCE_SPACECRAFT_HOLD_LOOP.wav",
                "drone.wav"
        );
//        audio.loopPlayForever("drone.wav", 0.1f);

        audio.load("resources/Shapeforms Audio Free Sound Effects/Cassette Preview/AUDIO/button.wav", "button.wav");
        audio.load("resources/Shapeforms Audio Free Sound Effects/future_ui/beep.wav", "caret.wav");
        audio.load("resources/Shapeforms Audio Free Sound Effects/type_preview/swipe.wav", "beep.wav");
        audio.load("resources/Shapeforms Audio Free Sound Effects/sci_fi_weapons/lock_on.wav", "select.wav");

        endOfUpdate.listenWith(consumer -> {
            consumer.accept(this);
        });
    }

    public Event<Consumer<Game>> deferred() {
        return endOfUpdate;
    }

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

        endOfUpdate.flush();
    }

    void onRender(Graphics2D graphics) {
        if (!stateStack.isEmpty()) {
            stateStack.peek().onRender(graphics);
        }
    }
}

package game.state;

import game.Game;

import java.awt.*;
import java.time.Duration;

public abstract class GameState {
    private final Game game;


    public GameState(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract void onUpdate(Duration delta);

    public abstract void onRender(Graphics2D graphics);

    public void onEnter() {
    }

    public void onExit() {
    }
}

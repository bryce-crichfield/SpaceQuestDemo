package game;

import game.title.MainMenuState;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        int tileSize = 16;
        Game game = new Game();
        Window window = new Window(640, 480, game);
        game.pushState(new MainMenuState(game));


        Instant lastTime = Instant.now();
        Duration delta = Duration.ZERO;

        while (true) {
            Instant currentTime = Instant.now();
            delta = Duration.between(lastTime, currentTime);
            lastTime = currentTime;

            game.onUpdate(delta);
            window.repaint();
        }
    }
}

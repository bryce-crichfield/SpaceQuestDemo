package game.state.title;

import game.state.GameState;
import game.util.Util;

import java.awt.*;
import java.time.Duration;

public class StarBackground {
    private final GameState gameState;
    private final float[][] stars = new float[175][10];
    private final int starX = 0;
    private final int starY = 1;
    private final int starSize = 2;
    private final int starSpeed = 3;
    private final int starRed = 4;
    private final int starGreen = 5;
    private final int starBlue = 6;
    private final int starWarble = 7;
    private final int starWarbleSpeed = 8;

    private final int starMinSize = 2;
    private final int starMaxSize = 8;


    public StarBackground(GameState gameState, int screenWidth, int screenHeight) {
        this.gameState = gameState;

        for (float[] star : stars) {
            star[starX] = Util.random(0, screenWidth);
            star[starY] = Util.random(0, screenHeight);

            float size = Util.random(starMinSize, starMaxSize, 1, -1.5f);
            star[starSize] = size;

            // big stars are slow, small stars are fast
            float speed = Util.map(size, starMinSize, starMaxSize, 20, 100);
            star[starSpeed] = speed;

            // big stars are bright, small stars are dim
            // bright stars are white, dim stars are red
            float saturation = Util.map(size, starMinSize, starMaxSize, 0.5f, 1);

            int rgb = Color.HSBtoRGB(0, 0, saturation);
            Color color = new Color(rgb);
            star[starRed] = color.getRed() / 255f;
            star[starGreen] = color.getGreen() / 255f;
            star[starBlue] = color.getBlue() / 255f;

            star[starWarble] = Util.random(10, 25);
            star[starWarbleSpeed] = Util.random(0.25f, 1f);
        }
    }

    public void onUpdate(Duration delta) {
        float dt = Util.perSecond(delta);
        for (float[] star : stars) {
            star[starY] -= star[starSpeed] * dt;
            if (star[starY] <= 0) {
                star[starY] = gameState.getGame().SCREEN_HEIGHT;
            }

            star[starWarble] += dt * star[starWarbleSpeed];
            if (star[starWarble] > 3) {
                star[starWarble] = 0;
            }
        }
    }

    public void onRender(Graphics2D graphics) {
        for (float[] star : stars) {
            float warble = star[starWarble];
            int x = (int) star[starX];
            int y = (int) star[starY];

            float sizeMod = (float) (Math.sin(warble * Math.PI * 2) * 2);
            int size = (int) star[starSize] + (int) sizeMod;
            int r = (int) (star[starRed] * 255);
            int g = (int) (star[starGreen] * 255);
            int b = (int) (star[starBlue] * 255);
            Color color = new Color(r, g, b);
            graphics.setColor(new Color(r, g, b));

            graphics.fillOval(x, y, size, size);
        }

    }
}

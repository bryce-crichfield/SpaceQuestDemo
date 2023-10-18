package game.form;

import game.Game;

import java.awt.*;

public class FormConst {
    public static final Color Black = new Color(0, 0, 0);
    public static final Color DarkGray = new Color(0x21, 0x21, 0x21);

    public static GradientPaint screenGradient(Color start, Color end) {
        return new GradientPaint(
                0, 0, start,
                Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, end
        );
    }
}

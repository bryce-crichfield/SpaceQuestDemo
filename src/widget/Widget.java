package widget;

import java.awt.*;
import java.time.Duration;

import game.Game;
import game.UserInterface;

public class Widget {
    Game game;
    String text;

    public Widget(String text, Game game) {
        this.text = text;
        this.game = game;
    }

    public void onUpdate(Duration delta) {

    }

    public void onRender(UserInterface ui, int menuX, int y, int menuWidth, boolean hovered) {
        if (hovered) {
            ui.setTextColor(UserInterface.highlight);
        }

        ui.drawTextRightJustified(text, menuX, y, menuWidth, 32, 32);
        ui.setTextColor(Color.WHITE);
    }
}

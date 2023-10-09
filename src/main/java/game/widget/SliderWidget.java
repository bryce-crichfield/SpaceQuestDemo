package game.widget;

import game.Game;
import game.io.Keyboard;
import game.util.Util;

import java.awt.*;
import java.time.Duration;

public class SliderWidget extends Widget {
    int padding = 32;
    int percent = 50;

    public SliderWidget(String text, Game game) {
        super(text, game);
    }

    public void onUpdate(Duration delta) {
        if (game.getKeyboard().pressed(Keyboard.LEFT)) {
            percent -= 10;
            game.getAudio().play("caret.wav");
            percent = Util.clamp(percent, 0, 100);
        }

        if (game.getKeyboard().pressed(Keyboard.RIGHT)) {
            percent += 10;
            game.getAudio().play("caret.wav");
            percent = Util.clamp(percent, 0, 100);
        }
    }

    @Override
    public void onRender(UserInterface ui, int menuX, int y, int menuWidth, boolean hovered) {
        super.onRender(ui, menuX, y, menuWidth, hovered);

        int sliderX = (int) (menuX + (menuWidth / 2f));
        int sliderY = y + 20;
        int sliderWidth = (int) ((menuWidth / 2f) - game.TILE_SIZE);
        int sliderHeight = ui.tileSize / 2;
        Color border = hovered ? UserInterface.highlight : Color.WHITE;
        ui.drawSlider(sliderX, sliderY, sliderWidth, sliderHeight, percent, border);
    }
}

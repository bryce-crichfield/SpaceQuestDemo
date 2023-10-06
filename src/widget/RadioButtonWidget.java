package widget;

import game.Game;
import game.Keyboard;
import game.UserInterface;

import java.awt.*;
import java.time.Duration;

public class RadioButtonWidget extends Widget {
    boolean selected = false;

    public RadioButtonWidget(String text, Game game) {
        super(text, game);
    }

    @Override
    public void onUpdate(Duration delta) {
        super.onUpdate(delta);

        if (game.getKeyboard().pressed(Keyboard.PRIMARY)) {
            selected = !selected;
            game.getAudio().play("caret.wav");
        }
    }

    @Override
    public void onRender(UserInterface ui, int menuX, int y, int menuWidth, boolean hovered) {
        super.onRender(ui, menuX, y, menuWidth, hovered);

        int radioX = (int) (menuX + (menuWidth / 2f));
        int radioY = y + 20;
        int radioHeight = ui.tileSize / 2;
        Color border = hovered ? UserInterface.highlight : Color.WHITE;

        ui.drawRadioButton(radioX, radioY, radioHeight, selected, border);
    }
}

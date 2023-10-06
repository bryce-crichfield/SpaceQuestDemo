package widget;

import game.Game;
import game.Keyboard;
import game.UserInterface;

import java.time.Duration;

public class ButtonWidget extends Widget {
    public boolean pressed = false;

    public ButtonWidget(String text, Game game) {
        super(text, game);
    }

    @Override
    public void onUpdate(Duration delta) {
        super.onUpdate(delta);

        pressed = false;

        if (game.getKeyboard().pressed(Keyboard.PRIMARY)) {
            pressed = true;
            game.getAudio().play("caret.wav");
        }
    }

    @Override
    public void onRender(UserInterface ui, int menuX, int y, int menuWidth, boolean hovered) {
        super.onRender(ui, menuX, y, menuWidth, hovered);
    }
}

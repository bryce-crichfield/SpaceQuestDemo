package widget;

import game.Game;
import game.Keyboard;
import game.UserInterface;

import java.time.Duration;

public class ButtonWidget extends Widget {
    private final Runnable action;

    public ButtonWidget(String text, Game game, Runnable action) {
        super(text, game);
        this.action = action;
    }


    @Override
    public void onUpdate(Duration delta) {
        super.onUpdate(delta);

        if (game.getKeyboard().pressed(Keyboard.PRIMARY)) {
            game.getAudio().play("caret.wav");
            action.run();
        }
    }

    @Override
    public void onRender(UserInterface ui, int menuX, int y, int menuWidth, boolean hovered) {
        super.onRender(ui, menuX, y, menuWidth, hovered);
    }
}

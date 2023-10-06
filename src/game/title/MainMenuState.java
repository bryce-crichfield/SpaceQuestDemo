package game.title;

import game.*;
import game.battle.PlayBattleState;
import game.overworld.PlayOverworldState;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class MainMenuState extends GameState {
    StarBackground starBackground;
    int caret = 0;
    List<Option> options = List.of(
            new Option("Overworld"),
            new Option("Battle"),
            new Option("Options"),
            new Option("Exit")
    );

    public MainMenuState(Game game) {
        super(game);
        starBackground = new StarBackground(this, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
    }

    @Override
    public void onUpdate(Duration delta) {
        if (getGame().getKeyboard().pressed(Keyboard.UP)) {
            caret--;
            getGame().getAudio().play("caret.wav");
        }

        if (getGame().getKeyboard().pressed(Keyboard.DOWN)) {
            caret++;
            getGame().getAudio().play("caret.wav");
        }
        caret = Util.wrap(caret, 0, options.size());

        Option currentOption = options.get(caret);
        if (getGame().getKeyboard().pressed(Keyboard.PRIMARY)) {
            getGame().getAudio().play("select.wav");
            if (currentOption.text.equals("Battle")) {
                getGame().pushState(new PlayBattleState(getGame()));
            } else if (currentOption.text.equals("Overworld")) {
                getGame().pushState(new PlayOverworldState(getGame()));
            }
            else if (currentOption.text.equals("Options")) {
                getGame().pushState(new OptionsMenuState(getGame()));
            } else if (currentOption.text.equals("Exit")) {
                System.exit(0);
            }
        }


        starBackground.onUpdate(delta);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(UserInterface.background);
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);
        starBackground.onRender(graphics);


        UserInterface ui = new UserInterface(graphics, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT,
                                             getGame().TILE_SIZE
        );

        int menuWidth = 5 * ui.tileSize;
        int menuHeight = 7 * ui.tileSize;
        int menuX = (ui.screenWidth / 2) - (menuWidth / 2);
        int menuY = (ui.screenHeight / 2) - (menuHeight / 2);
        menuY += ui.tileSize;

        ui.textSize = 32;
        ui.textColor = UserInterface.highlight;
        ui.drawTextCentered("Space Quest", 0, 16, ui.screenWidth, 32);


        // draw the menu
        ui.textColor = Color.WHITE;
        ui.drawPanel(menuX, menuY, menuWidth, menuHeight);


        int optionsStartY = menuY + 8;
        ui.textSize = 16;

        int y = optionsStartY;
        for (Option option : options) {
            boolean selected = options.indexOf(option) == caret;
            option.onRender(ui, menuX, y, menuWidth, selected);
            y += ui.tileSize;
        }

        int caretY = optionsStartY + caret * ui.tileSize;
        ui.drawTextRightJustified(">", menuX, caretY, menuWidth, 32, 12);
    }

    private static class Option {
        String text;


        public Option(String text) {
            this.text = text;
        }

        public void onRender(UserInterface ui, int menuX, int y, int menuWidth, boolean hovered) {
            if (hovered) {
                ui.textColor = UserInterface.highlight;
            }

            ui.drawTextRightJustified(text, menuX, y, menuWidth, 32, 32);
            ui.textColor = Color.WHITE;
        }
    }

}

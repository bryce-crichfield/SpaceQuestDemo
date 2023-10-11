package game.state.options;

import game.Game;
import game.state.GameState;
import game.widget.Menu;
import game.widget.*;

import java.awt.*;
import java.time.Duration;

public class OptionsMenuState extends GameState {
    private final Menu menu;
    private final int caret = 0;


    public OptionsMenuState(Game game) {
        super(game);

        var musicVolume = new SliderWidget("Music Volume", getGame());
        var soundVolume = new SliderWidget("Sound Volume", getGame());
        var fullscreen = new RadioButtonWidget("Fullscreen", getGame());

        var controls = new ButtonWidget("Controls", getGame(), () -> {
            getGame().pushState(new ControlsMenuState(getGame()));
        });

        var back = new ButtonWidget("Back", getGame(), () -> {
            getGame().popState();
        });


        int menuWidth = 11 * Game.TILE_SIZE;
        int menuHeight = 7 * Game.TILE_SIZE;
        int menuX = (Game.SCREEN_WIDTH / 2) - (menuWidth / 2);
        int menuY = (Game.SCREEN_HEIGHT / 2) - (menuHeight / 2);
        menu = new Menu(getGame(), menuX, menuY, menuWidth, menuHeight);
        menu.setWidgets(musicVolume, soundVolume, fullscreen, controls, new Blank(getGame()), back);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onUpdate(Duration delta) {
        menu.onUpdate(delta);
    }

    @Override
    public void onRender(Graphics2D graphics) {


        UserInterface ui = new UserInterface(graphics, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT,
                                             Game.TILE_SIZE
        );
        graphics.setColor(ui.background);
        graphics.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        ui.textSize = 24;
        ui.textColor = ui.highlight;
        ui.drawTextCentered("Options Menu", 0, 16, ui.screenWidth, 32);

        menu.onRender(graphics);
    }
}

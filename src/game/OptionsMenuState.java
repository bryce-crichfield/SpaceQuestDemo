package game;

import widget.ButtonWidget;
import widget.Menu;
import widget.RadioButtonWidget;
import widget.SliderWidget;

import java.awt.*;
import java.time.Duration;

public class OptionsMenuState extends GameState {
    private int caret = 0;

    private final SliderWidget musicVolume;
    private final SliderWidget soundVolume;
    private final RadioButtonWidget fullscreen;
    private final ButtonWidget controls;
    private final ButtonWidget back;
    private final widget.Menu menu;


    public OptionsMenuState(Game game) {
        super(game);

        musicVolume = new SliderWidget("Music Volume", getGame());
        soundVolume = new SliderWidget("Sound Volume", getGame());
        fullscreen = new RadioButtonWidget("Fullscreen", getGame());
        controls = new ButtonWidget("Controls", getGame());
        back = new ButtonWidget("Back", getGame());


        int menuWidth = 11 * getGame().TILE_SIZE;
        int menuHeight = 7 * getGame().TILE_SIZE;
        int menuX = (getGame().SCREEN_WIDTH / 2) - (menuWidth / 2);
        int menuY = (getGame().SCREEN_HEIGHT / 2) - (menuHeight / 2);
        menu = new Menu(getGame(), menuX, menuY, menuWidth, menuHeight);
        menu.setWidgets(musicVolume, soundVolume, fullscreen, controls, back);
    }

    @Override
    public void onUpdate(Duration delta) {
        menu.onUpdate(delta);

        if (back.pressed) {
            getGame().popState();
        }

        if (controls.pressed) {
            getGame().pushState(new ControlsMenuState(getGame()));
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(UserInterface.background);
        graphics.fillRect(0, 0, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT);

        UserInterface ui = new UserInterface(graphics, getGame().SCREEN_WIDTH, getGame().SCREEN_HEIGHT, getGame().TILE_SIZE);
        ui.textSize = 24;
        ui.textColor = UserInterface.highlight;
        ui.drawTextCentered("Options Menu", 0, 16, ui.screenWidth, 32);

        menu.onRender(graphics);
    }
}

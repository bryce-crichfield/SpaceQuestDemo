package game.state.title;

import game.Game;
import game.state.GameState;
import game.state.options.OptionsMenuState;
import game.widget.UserInterface;
import game.state.battle.BattleState;
import game.state.overworld.PlayOverworldState;
import game.widget.ButtonWidget;
import game.widget.Menu;

import java.awt.*;
import java.time.Duration;

public class MainMenuState extends GameState {
    private final Menu menu;
    StarBackground starBackground;


    public MainMenuState(Game game) {
        super(game);
        starBackground = new StarBackground(this, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        int menuWidth = 5 * getGame().TILE_SIZE;
        int menuHeight = 7 * getGame().TILE_SIZE;
        int menuX = (getGame().SCREEN_WIDTH / 2) - (menuWidth / 2);
        int menuY = (getGame().SCREEN_HEIGHT / 2) - (menuHeight / 2);

        menu = new Menu(getGame(), menuX, menuY, menuWidth, menuHeight);
        menu.setWidgets(
                new ButtonWidget("Overworld", getGame(), () -> {
                    getGame().pushState(new PlayOverworldState(getGame()));
                }),

                new ButtonWidget("Battle", getGame(), () -> {
                    getGame().pushState(new BattleState(getGame()));
                }),

                new ButtonWidget("Options", getGame(), () -> {
                    getGame().pushState(new OptionsMenuState(getGame()));
                }),

                new ButtonWidget("Exit", getGame(), () -> {
                    System.exit(0);
                })
        );

        menu.setItemDistance(32);
        menu.setTextSize(16);
    }

    @Override
    public void onUpdate(Duration delta) {
        menu.onUpdate(delta);
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

        ui.textSize = 32;
        ui.textColor = UserInterface.highlight;
        ui.drawTextCentered("Space Quest", 0, 16, ui.screenWidth, 32);

        menu.onRender(graphics);
    }
}

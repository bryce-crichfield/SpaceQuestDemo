package game.state.title;

import game.Game;
import game.form.element.FormLabel;
import game.form.element.FormMenu;
import game.form.properties.FormAlignment;
import game.form.properties.FormFill;
import game.form.properties.FormText;
import game.io.Keyboard;
import game.state.GameState;
import game.state.battle.BattleState;
import game.state.options.OptionsMenuState;
import game.state.overworld.PlayOverworldState;

import java.awt.*;
import java.time.Duration;

public class MainMenuState extends GameState {
    StarBackground starBackground;
    FormMenu menu;

    public MainMenuState(Game game) {
        super(game);

        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        menu = new FormMenu(25, 25, 50, 50);
        menu.setElementAlignment(FormAlignment.CENTER);
        int menuX = (int) menu.getAbsoluteBounds().getX();
        int menuWidth = (int) menu.getAbsoluteBounds().getWidth();
        int menuY = (int) menu.getAbsoluteBounds().getY();
        int menuHeight = (int) menu.getAbsoluteBounds().getHeight();

        Paint paint = new GradientPaint(menuX, menuY, Color.BLACK, menuX + menuWidth, menuY + menuHeight,
                                        Color.DARK_GRAY
        );
        FormFill formFill = new FormFill();
        formFill.setPaint(paint);
        formFill.setRoundness(25);
        menu.setFill(formFill);


        FormLabel title = new FormLabel(100, 20);
        FormText text = new FormText();
        text.setValue("Space Quest");
        text.setSize(32);
        title.setText(text);
        menu.addChild(title);

        String textPadding = "   ";

        FormLabel overworld = createMenuOption(textPadding + "Overworld", () -> {
            game.deferred().fire(g -> g.pushState(new PlayOverworldState(g)));
        });
        menu.addCaretChild(overworld);

        FormLabel battle = createMenuOption(textPadding + "Battle", () -> {
            game.deferred().fire(g -> g.pushState(new BattleState(g)));
        });
        menu.addCaretChild(battle);

        FormLabel options = createMenuOption(textPadding + "Options", () -> {
            game.deferred().fire(g -> g.pushState(new OptionsMenuState(g)));
        });
        menu.addCaretChild(options);

        FormLabel quit = createMenuOption(textPadding + "Quit", () -> {
            System.exit(0);
        });
        menu.addCaretChild(quit);
    }

    private FormLabel createMenuOption(String text, Runnable action) {
        FormLabel option = new FormLabel(100, 20);

        FormText formText = new FormText();
        formText.setValue(text);
        formText.setSize(16);
        option.setText(formText);

        option.setHorizontalTextAlignment(FormAlignment.START);
        option.onPrimary.listenWith((e) -> action.run());

        return option;
    }

    @Override
    public void onEnter() {
        getSubscriptions().on(getOnGuiRender()).run(starBackground::onRender);
        getSubscriptions().on(getOnGuiRender()).run(menu::onRender);
        getSubscriptions().on(Keyboard.keyPressed).run(menu::onKeyPressed);
    }

    @Override
    public void onUpdate(Duration delta) {
        starBackground.onUpdate(delta);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        getOnGuiRender().fire(graphics);
    }
}

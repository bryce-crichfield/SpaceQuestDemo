package game.state.title;

import game.Game;
import game.form.element.FormElement;
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
    FormElement container;
    FormMenu menu;

    public MainMenuState(Game game) {
        super(game);

        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        container = new FormElement(25, 25, 50, 50);

        menu = new FormMenu(0, 0, 100, 2);
        menu.setElementAlignment(FormAlignment.START);
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


        FormElement title = new FormElement(100, 1);
        FormText text = new FormText();
        text.setValue("Space Quest");
        text.setSize(32);
        title.setText(text);
        container.addChild(title);

        String textPadding = "   ";

        FormElement overworld = createMenuOption(textPadding + "Overworld", 1, () -> {
            game.deferred().fire(g -> g.pushState(new PlayOverworldState(g)));
        });
        menu.addCaretChild(overworld);

        FormElement battle = createMenuOption(textPadding + "Battle",1, () -> {
            game.deferred().fire(g -> g.pushState(new BattleState(g)));
        });
        menu.addCaretChild(battle);

        FormElement options = createMenuOption(textPadding + "Options",1, () -> {
            game.deferred().fire(g -> g.pushState(new OptionsMenuState(g)));
        });
        menu.addCaretChild(options);

        FormElement quit = createMenuOption(textPadding + "Quit", 1,() -> {
            System.exit(0);
        });
        menu.addCaretChild(quit);

        menu.getLayout().execute(menu);

        container.addChild(menu);
        container.getLayout().execute(container);
    }

    private FormElement createMenuOption(String text, int height, Runnable action) {
        FormElement option = new FormElement(100, height);

        FormText formText = new FormText();
        formText.setValue(text);
        formText.setSize(16);
        option.setText(formText);
        option.setElementAlignment(FormAlignment.START);
        option.setHorizontalTextAlignment(FormAlignment.START);
        option.getOnPrimary().listenWith((e) -> action.run());

        return option;
    }

    @Override
    public void onEnter() {
        getSubscriptions().on(getOnGuiRender()).run(starBackground::onRender);
        getSubscriptions().on(getOnGuiRender()).run(container::onRender);
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

package game.state.title;

import game.Game;
import game.form.element.FormLabel;
import game.form.element.FormMenu;
import game.form.properties.FormAlignment;
import game.form.properties.FormFill;
import game.io.Keyboard;
import game.state.GameState;
import game.state.battle.BattleState;
import game.state.overworld.PlayOverworldState;

import java.awt.*;
import java.time.Duration;
import java.util.Optional;

public class MainMenuState extends GameState {
    StarBackground starBackground;
    FormMenu menu;

    public MainMenuState(Game game) {
        super(game);
        starBackground = new StarBackground(this, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        menu = new FormMenu(25, 25, 50, 50);
        menu.elementAlignment.set(FormAlignment.CENTER);
        int menuX = (int) menu.getAbsoluteBounds().getX();
        int menuWidth = (int) menu.getAbsoluteBounds().getWidth();
        int menuY = (int) menu.getAbsoluteBounds().getY();
        int menuHeight = (int) menu.getAbsoluteBounds().getHeight();

        Paint paint = new GradientPaint(menuX, menuY, Color.BLACK, menuX + menuWidth, menuY + menuHeight,
                                        Color.DARK_GRAY
        );
        menu.fill.set(Optional.of(new FormFill(paint)));

        FormLabel title = new FormLabel(100, 25);
        title.text.set(text -> text.withValue("Space Quest"));
        title.text.set(text -> text.withSize(32));
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


        FormLabel quit = createMenuOption(textPadding + "Quit", () -> {
            System.exit(0);
        });
        menu.addCaretChild(quit);
    }

    private FormLabel createMenuOption(String text, Runnable action) {
        FormLabel option = new FormLabel(100, 25);
        option.text.set(textValue -> textValue.withValue(text));
        option.text.set(textValue -> textValue.withSize(16));
        option.horizontalTextAlignment.set(FormAlignment.START);
        option.onPrimary.listenWith((event) -> {
            action.run();
        });
        menu.onCaretHighlight.listenWith(element -> {
            if (element.equals(option)) {
                option.text.set(t -> t.withFill(Color.ORANGE));
            } else {
                option.text.set(t -> t.withFill(Color.WHITE));
            }
        });
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

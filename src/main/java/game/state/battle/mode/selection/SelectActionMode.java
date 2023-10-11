package game.state.battle.mode.selection;

import game.Game;
import game.event.SubscriptionManager;
import game.form.element.FormLabel;
import game.form.element.FormMenu;
import game.form.properties.FormAlignment;
import game.form.properties.FormFill;
import game.form.properties.FormVerticalLayout;
import game.io.Keyboard;
import game.state.battle.BattleState;
import game.state.battle.event.ActorDeselected;
import game.state.battle.event.ModeChanged;
import game.state.battle.mode.ActionMode;
import game.state.battle.mode.ObserverMode;
import game.state.battle.mode.attack.AttackActionMode;
import game.state.battle.mode.move.MoveActionMode;
import game.state.battle.world.Actor;

import java.awt.*;
import java.time.Duration;
import java.util.Optional;

public class SelectActionMode extends ActionMode {
    private final BattleState battleState;
    private final Actor actor;
    private final FormMenu menu;

    public SelectActionMode(BattleState battleState, Actor actor) {
        super(battleState);
        this.battleState = battleState;
        this.actor = actor;
        Game game = battleState.getGame();

        int percentWidth = 25;
        int percentHeight = 32;
        int rightJustified = 100 - percentWidth - 5;
        int bottomJustified = 100 - percentHeight - 5;

        menu = new FormMenu(rightJustified, bottomJustified, percentWidth, percentHeight);
        menu.fill.set(Optional.of(new FormFill(Color.BLACK, 25)));
        menu.layout.set(new FormVerticalLayout());


        String textPadding = "   ";
        FormLabel attack = createMenuOption(textPadding+"Attack", () -> {
            ModeChanged.event.fire(new AttackActionMode(battleState, actor));
        });
        menu.addCaretChild(attack);

        FormLabel move = createMenuOption(textPadding+"Move", () -> {
            ModeChanged.event.fire(new MoveActionMode(battleState, actor));
        });
        menu.addCaretChild(move);

        FormLabel item = createMenuOption(textPadding+"Item", () -> {
        });
        menu.addCaretChild(item);

        FormLabel wait = createMenuOption(textPadding+"Wait", () -> {
        });
        menu.addCaretChild(wait);
    }

    @Override
    public void onEnter() {
        on(battleState.getOnGuiRender()).run(menu::onRender);
        on(Keyboard.keyPressed).run(menu::onKeyPressed);
        on(Keyboard.keyPressed).run(keyCode -> {
            if (keyCode == Keyboard.SECONDARY) {
                ActorDeselected.event.fire(new ActorDeselected(actor));
                ModeChanged.event.fire(new ObserverMode(battleState));
            }
        });
    }

    @Override
    public void onUpdate(Duration delta) {
        battleState.getCursor().onUpdate(delta);
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
}

package game.state.battle.hud;

import game.form.element.FormElement;
import game.form.element.FormMenu;
import game.form.properties.*;
import game.state.battle.player.*;
import game.state.battle.event.ControllerTransition;
import game.state.battle.model.Actor;
import game.util.Util;

import java.awt.*;

public class HudActions extends FormMenu {
    private PlayerMode controller;

    public HudActions(int x, int y, PlayerMode controller) {
        this(x, y);
        this.controller = controller;
    }

    private static final int WIDTH = 160;
    private static final int HEIGHT = 95;

    private final FormElement title = Util.pure(() -> {
        FormElement title = new FormElement(WIDTH, 20);
        FormText text = new FormText();
        text.setValue("Actions");
        text.setSize(16);
        title.setText(text);
        title.setHorizontalTextAlignment(FormAlignment.START);
        title.setVerticalTextAlignment(FormAlignment.CENTER);
        title.setPadding(new FormMargin(0, 0, 3, 0));

        return title;
    });

    private final FormElement attackOption = Util.pure(() -> {
        FormElement option = createMenuOption("Attack");
        option.getOnPrimary().listenWith((e) -> {
            // When the attack option is selected, we want to transition to the
            // attack controller, which will allow the player to select a target
            ControllerTransition.defer.fire(() -> new SelectAttackPlayerMode(controller));
        });

        return option;
    });

    private final FormElement moveOption = Util.pure(() -> {
        FormElement option = createMenuOption("Move");
        option.getOnPrimary().listenWith((e) -> {
            // When the move option is selected, we want to transition to the
            // move controller, which will allow the player to move the selected
            // actor to a new tile
            ControllerTransition.defer.fire(() -> new SelectMovePlayerMode(controller));
        });

        return option;
    });

    private final FormElement itemOption = Util.pure(() -> {
        FormElement option = createMenuOption("Item");
        option.getOnPrimary().listenWith((e) -> {
            // When the item option is selected, we want to transition to the
            // item controller, which will allow the player to select an item
            // to use on the selected actor
            ControllerTransition.defer.fire(() -> new SelectItemPlayerMode(controller));
        });

        return option;
    });

    private final FormElement waitOption = Util.pure(() -> {
        FormElement option = createMenuOption("Wait");
        option.getOnPrimary().listenWith((e) -> {
            // When the wait option is selected, we want to transition to the
            // observer controller, which will allow the player to select a new
            // actor
            ControllerTransition.defer.fire(() -> {
                Actor actor = controller.getActor().get();
                actor.setWaiting(true);
                controller.deselectActor();
                return new ObserverPlayerMode(controller);
            });
        });

        return option;
    });

    private HudActions(int x, int y) {
        super(x, y, WIDTH, HEIGHT);

        addChild(title);
        addCaretChild(attackOption);
        addCaretChild(moveOption);
        addCaretChild(itemOption);
        addCaretChild(waitOption);
    }

    public static FormElement createMenuOption(String text) {
        FormElement option = new FormElement(WIDTH / 2, 15);
        option.setHorizontalTextAlignment(FormAlignment.START);

        // Small text on not hovered
        FormText defaultText = new FormText().setValue(text).setSize(12).setFill(Color.WHITE);
        option.setText(defaultText);
        option.getOnUnhover().listenWith((e) -> option.setText(defaultText));

        // Large text on hover
        FormText hoverText = new FormText().setValue(text).setSize(14).setFill(Color.RED);
        option.getOnHover().listenWith((e) -> option.setText(hoverText));

        return option;
    }
}

package game.state.battle.hud;

import game.form.element.FormElement;
import game.form.element.FormMenu;
import game.form.properties.*;
import game.form.properties.layout.FormVerticalLayout;
import game.state.battle.controller.ObserverPlayerController;
import game.state.battle.controller.SelectAttackPlayerController;
import game.state.battle.controller.SelectMovePlayerController;
import game.state.battle.event.ActorUnselected;
import game.state.battle.event.ControllerTransition;
import game.state.battle.model.actor.Actor;
import game.event.Event;

import java.awt.*;
import java.util.Optional;

public class HudActions extends FormMenu {
    private final Event<Actor> onActorChanged;

    public HudActions(int x, int y, int width, int height, Event<Actor> onActorChanged) {
        super(x, y, width, height);
        this.onActorChanged = onActorChanged;

        Color barelyBlack = new Color(0x21, 0x21, 0x21, 0xff);
        Paint gradient = new GradientPaint(0, 200, barelyBlack, 0, 400, Color.BLACK);
        setFill(new FormFill(gradient, 25));
        setLayout(new FormVerticalLayout());

        FormElement title = new FormElement(100, 30);
        FormText text = new FormText();
        text.setValue("Actions");
        text.setSize(22);
        title.setText(text);
        title.setHorizontalTextAlignment(FormAlignment.CENTER);
        title.setVerticalTextAlignment(FormAlignment.CENTER);
        addChild(title);


        FormElement v1 = new FormElement(100, 5);
        addChild(v1);

        String textPadding = "   ";
        FormElement attack = createMenuOption(textPadding + "Attack", () -> {
            ControllerTransition.defer.fire((state) -> {
                Optional<Actor> selected = state.getSelector().getCurrentlySelectedActor();
                if (selected.isEmpty()) {
                    throw new RuntimeException("Invalid state, no actor selected");
                }
                return new SelectAttackPlayerController(state, selected.get());
            });
        });

        addCaretChild(attack);

        FormElement move = createMenuOption(textPadding + "Move", () -> {
            ControllerTransition.defer.fire((state) -> {
                Optional<Actor> selected = state.getSelector().getCurrentlySelectedActor();
                if (selected.isEmpty()) {
                    throw new RuntimeException("Invalid state, no actor selected");
                }
                return new SelectMovePlayerController(state, selected.get());
            });
        });
        addCaretChild(move);

        FormElement item = createMenuOption(textPadding + "Item", () -> {
        });
        addCaretChild(item);

        FormElement wait = createMenuOption(textPadding + "Wait", () -> {
            ControllerTransition.defer.fire((state) -> {
                Optional<Actor> selected = state.getSelector().getCurrentlySelectedActor();
                if (selected.isEmpty()) {
                    throw new RuntimeException("Invalid state, no actor selected");
                }

                Actor actor = selected.get();
                actor.setWaiting(true);
                ActorUnselected.event.fire(actor);
                state.getSelector().deselectActor();
                return new ObserverPlayerController(state);
            });
        });
        addCaretChild(wait);

        FormElement v2 = new FormElement(100, 5);
        addChild(v2);


        getLayout().execute(this);
    }

    private FormElement createMenuOption(String text, Runnable action) {
        FormElement option = new FormElement(100, 20);

        FormText formText = new FormText();
        formText.setValue(text);
        formText.setSize(16);
        option.setText(formText);

        option.setHorizontalTextAlignment(FormAlignment.START);
        option.getOnPrimary().listenWith((e) -> action.run());

        return option;
    }
}

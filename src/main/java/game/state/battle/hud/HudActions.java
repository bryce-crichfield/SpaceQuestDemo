package game.state.battle.hud;

import game.form.element.FormElement;
import game.form.element.FormMenu;
import game.form.properties.*;
import game.form.properties.layout.FormVerticalLayout;
import game.state.battle.controller.ObserverModalController;
import game.state.battle.controller.SelectAttackModalController;
import game.state.battle.controller.SelectMoveModalController;
import game.state.battle.event.ActorDamaged;
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

        setFill(new FormFill(Color.BLACK, 25));
        setLayout(new FormVerticalLayout());

        FormElement title = new FormElement(100, 15);
        FormText text = new FormText();
        text.setValue("Actions");
        text.setSize(22);
        title.setText(text);
        title.setHorizontalTextAlignment(FormAlignment.CENTER);
        title.setVerticalTextAlignment(FormAlignment.CENTER);
        addChild(title);

        String textPadding = "   ";
        FormElement attack = createMenuOption(textPadding + "Attack", () -> {
            ControllerTransition.defer.fire((state) -> {
                Optional<Actor> selected = state.getSelector().getCurrentlySelectedActor();
                if (selected.isEmpty()) {
                    throw new RuntimeException("Invalid state, no actor selected");
                }
                return new SelectAttackModalController(state);
            });
        });

        addCaretChild(attack);

        FormElement move = createMenuOption(textPadding + "Move", () -> {
            ControllerTransition.defer.fire((state) -> {
                Optional<Actor> selected = state.getSelector().getCurrentlySelectedActor();
                if (selected.isEmpty()) {
                    throw new RuntimeException("Invalid state, no actor selected");
                }
                return new SelectMoveModalController(state);
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
                return new ObserverModalController(state);
            });
        });
        addCaretChild(wait);

        setMargin(new FormMargin(0, 0, 5, 0));

        getLayout().execute(this);
    }

    private FormElement createMenuOption(String text, Runnable action) {
        FormElement option = new FormElement(100, 10);

        FormText formText = new FormText();
        formText.setValue(text);
        formText.setSize(16);
        option.setText(formText);

        option.setHorizontalTextAlignment(FormAlignment.START);
        option.getOnPrimary().listenWith((e) -> action.run());

        return option;
    }
}

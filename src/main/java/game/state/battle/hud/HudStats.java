package game.state.battle.hud;

import game.event.Event;
import game.form.element.FormElement;
import game.form.properties.*;
import game.form.properties.layout.FormHorizontalLayout;
import game.state.battle.model.Actor;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HudStats extends FormElement {
    private final Event<Actor> onActorChanged;

    public HudStats(int x, int y, int width, int height, Event<Actor> onActorChanged) {
        super(x, y, width, height);

        setMargin(new FormMargin(5, 5, 5, 5));

        this.onActorChanged = onActorChanged;

        FormFill fill = new FormFill();
        fill.setPaint(Color.DARK_GRAY.darker().darker().darker());
        fill.setRoundness(25);
        setFill(fill);

        FormBorder border = new FormBorder();
        setBorder(border);

        createStatSheetEntry("", (actorName, actor) -> {
            actorName.getText().setValue(actor.getName());
        });

        createStatSheetEntry("Health", (actorHealth, actor) -> {
            String health = String.valueOf(actor.getHealth());
            actorHealth.getText().setValue(health);
        });

        createStatSheetEntry("Position", (actorPosition, actor) -> {
            String positionX = String.valueOf((int) actor.getX());
            String positionY = String.valueOf((int) actor.getY());
            String position = positionX + ", " + positionY;
            actorPosition.getText().setValue(position);
        });

        createStatSheetEntry("Movement", (actorMovement, actor) -> {
            String movement = String.valueOf(actor.getMovementPoints());
            actorMovement.getText().setValue(movement);
        });

        createStatSheetEntry("Range", (actorRange, actor) -> {
            String range = String.valueOf(actor.getAttackDistance());
            actorRange.getText().setValue(range);
        });

        createStatSheetEntry("Attack", (actorAttack, actor) -> {
            String attack = String.valueOf(actor.getAttack());
            actorAttack.getText().setValue(attack);
        });

        createStatSheetEntry("Defense", (actorDefense, actor) -> {
            String defense = String.valueOf(0);
            actorDefense.getText().setValue(defense);
        });


        getLayout().execute(this);
    }

    private void createStatSheetEntry(String name, BiConsumer<FormElement, Actor> update) {
        FormElement row = new FormElement("");
        row.getBounds().setWidth(100);
        row.getBounds().setHeight(1);
        row.setLayout(new FormHorizontalLayout());

        if (!name.equals("")) {
            FormElement label = new FormElement(name);
            label.getBounds().setWidth(75);
            label.getBounds().setHeight(1);
            label.getText().setSize(12);
            row.addChild(label);
        }

        FormElement element = new FormElement("");

        FormText text = new FormText();
        element.getBounds().setWidth(25);
        element.getBounds().setHeight(1);
        element.getText().setSize(12);
        row.addChild(element);

        onActorChanged.listenWith(actor -> update.accept(element, actor));

        addChild(row);
    }
}

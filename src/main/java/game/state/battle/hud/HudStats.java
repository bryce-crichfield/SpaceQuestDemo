package game.state.battle.hud;

import game.event.Event;
import game.form.element.FormElement;
import game.form.properties.*;
import game.form.properties.layout.FormHorizontalLayout;
import game.state.battle.model.actor.Actor;

import java.awt.*;
import java.util.function.BiConsumer;

public class HudStats extends FormElement {
    private final int vSpace = 10;
    private final int rowHeight = 20;
    private final int titleHeight = 20;


    private final Event<Actor> onActorChanged;

    public HudStats(int x, int y, Event<Actor> onActorChanged) {
        super(x, y, 150, 200);

        this.onActorChanged = onActorChanged;

        FormFill fill = new FormFill();
        Color barelyBlack = new Color(0x21, 0x21, 0x21, 0xff);
        Paint gradient = new GradientPaint(0, 0, barelyBlack, 0, 400, Color.BLACK);
        fill.setPaint(gradient);
        fill.setRoundness(25);
        setFill(fill);

        FormBorder border = new FormBorder();
        setBorder(border);

        FormElement rowContainer = new FormElement("");
        rowContainer.getBounds().setWidth(100);
        rowContainer.getBounds().setHeight(100);

        createPadding(rowContainer);

        createTitleEntry(rowContainer, (actorName, actor) -> {
            actorName.getText().setValue(actor.getName());
        });

        createPadding(rowContainer);

        createStatSheetEntry(rowContainer, "Health", (actorHealth, actor) -> {
            String health = String.valueOf(actor.getHealth());
            actorHealth.getText().setValue(health);
        });

        createStatSheetEntry(rowContainer, "Position", (actorPosition, actor) -> {
            String positionX = String.valueOf((int) actor.getX());
            String positionY = String.valueOf((int) actor.getY());
            String position = positionX + ", " + positionY;
            actorPosition.getText().setValue(position + " ");
        });

        createStatSheetEntry(rowContainer, "Movement", (actorMovement, actor) -> {
            String movement = String.valueOf(actor.getMovementPoints());
            actorMovement.getText().setValue(movement);
        });

        createStatSheetEntry(rowContainer, "Range", (actorRange, actor) -> {
            String range = String.valueOf(actor.getAttackDistance());
            actorRange.getText().setValue(range);
        });

        createStatSheetEntry(rowContainer, "Attack", (actorAttack, actor) -> {
            String attack = String.valueOf(actor.getAttack());
            actorAttack.getText().setValue(attack);
        });

        createStatSheetEntry(rowContainer, "Defense", (actorDefense, actor) -> {
            String defense = String.valueOf(0);
            actorDefense.getText().setValue(defense);
        });

        createPadding(rowContainer);

        addChild(rowContainer);
        onLayout();
    }

    private void createTitleEntry(FormElement rowContainer, BiConsumer<FormElement, Actor> update) {
        FormElement row = new FormElement("");
        row.getBounds().setWidth(150);
        row.getBounds().setHeight(titleHeight);
        row.setLayout(new FormHorizontalLayout());

        onActorChanged.listenWith(actor -> update.accept(row, actor));

        rowContainer.addChild(row);
    }

    private void createPadding(FormElement rowContainer) {
        FormElement row = new FormElement("");
        row.getBounds().setWidth(150);
        row.getBounds().setHeight(vSpace);
        row.setLayout(new FormHorizontalLayout());


        rowContainer.addChild(row);
    }
    private void createStatSheetEntry(FormElement rowContainer, String name, BiConsumer<FormElement, Actor> update) {
        FormElement row = new FormElement("");
        row.getBounds().setWidth(150);
        row.getBounds().setHeight(rowHeight);
        row.setLayout(new FormHorizontalLayout());

        if (!name.equals("")) {
            FormElement label = new FormElement(" " + name);
            label.getBounds().setWidth(75);
            label.getBounds().setHeight(1);
            label.getText().setSize(12);
            label.setHorizontalTextAlignment(FormAlignment.START);
            row.addChild(label);
        }

        FormElement element = new FormElement("");
        FormText text = new FormText();
        element.getBounds().setWidth(25);
        element.getBounds().setHeight(1);
        element.getText().setSize(12);
        element.setHorizontalTextAlignment(FormAlignment.START);
        row.addChild(element);

        onActorChanged.listenWith(actor -> update.accept(element, actor));

        rowContainer.addChild(row);
    }
}

package game.state.battle.hud;

import game.event.Event;
import game.form.element.FormElement;
import game.form.properties.FormBorder;
import game.form.properties.FormBounds;
import game.form.properties.FormFill;
import game.form.properties.layout.FormHorizontalLayout;
import game.state.battle.model.Actor;

import java.awt.*;
import java.util.function.Consumer;

public class HudStats extends FormElement {
    private final Event<Actor> onActorChanged;

    public HudStats(int x, int y, int width, int height, Event<Actor> onActorChanged) {
        super(x, y, width, height);

        this.onActorChanged = onActorChanged;

        FormFill fill = new FormFill();
        fill.setPaint(Color.DARK_GRAY.darker().darker().darker());
        fill.setRoundness(25);
        setFill(fill);

        FormBorder border = new FormBorder();
        setBorder(border);

        FormElement actorName = new FormElement("");
        FormElement actorPosition = new FormElement("");
        FormElement actorHealth = new FormElement("");
        FormElement actorMovement = new FormElement("");
        FormElement actorRange = new FormElement("");

        addChild(createStatSheetEntry("Name", actorName, actor -> {
            actorName.getText().setValue(actor.getName());
        }));

        addChild(createStatSheetEntry("Health", actorHealth, actor -> {
            String health = String.valueOf(actor.getHealth());
            actorHealth.getText().setValue(health);
        }));

        addChild(createStatSheetEntry("Position", actorPosition, actor -> {
            String positionX = String.valueOf((int) actor.getX());
            String positionY = String.valueOf((int) actor.getY());
            String position = positionX + ", " + positionY;
            actorPosition.getText().setValue(positionX);
        }));

        addChild(createStatSheetEntry("Movement", actorMovement, actor -> {
            String movement = String.valueOf(actor.getMovementPoints());
            actorMovement.getText().setValue(movement);
        }));

        addChild(createStatSheetEntry("Range", actorRange, actor -> {
            String range = String.valueOf(actor.getAttackDistance());
            actorRange.getText().setValue(range);
        }));

        getLayout().execute(this);
    }

    private FormElement createStatSheetEntry(String name, FormElement element, Consumer<Actor> update) {
        FormElement row = new FormElement("");
        row.getBounds().setWidth(100);
        row.getBounds().setHeight(1);
        row.setLayout(new FormHorizontalLayout());

        FormElement label = new FormElement(name);
        label.getBounds().setWidth(50);
        label.getBounds().setHeight(1);
        row.addChild(label);

        element.getBounds().setWidth(50);
        element.getBounds().setHeight(1);
        row.addChild(element);

        onActorChanged.listenWith(update::accept);

        return row;
    }
}
